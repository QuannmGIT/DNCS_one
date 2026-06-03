param(
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$DB_NAME = "StoreManagement"
$DB_USER = "root"
$DB_PASS = ""

function Write-Step($msg) {
    Write-Host "`n==> $msg" -ForegroundColor Cyan
}

function Write-Info($msg) {
    Write-Host "  $msg" -ForegroundColor Gray
}

function Write-Success($msg) {
    Write-Host "  $msg" -ForegroundColor Green
}

function Write-Warn($msg) {
    Write-Host "  $msg" -ForegroundColor Yellow
}

# ---------------------------------------------------------------
# 1. Check Laragon
# ---------------------------------------------------------------
Write-Step "1. Checking Laragon installation"

$LaragonPaths = @(
    "$env:ProgramFiles\Laragon",
    "$env:ProgramFiles(x86)\Laragon",
    "$env:LOCALAPPDATA\Laragon",
    "C:\Laragon"
)

$LaragonBin = $null
foreach ($p in $LaragonPaths) {
    $candidate = "$p\usr\bin\mysql.exe"
    if (Test-Path $candidate) {
        $LaragonBin = "$p\usr\bin"
        Write-Success "Laragon found at $p"
        break
    }
}

if (-not $LaragonBin) {
    Write-Warn "Laragon not found. Downloading and installing Laragon..."
    $installer = "$env:TEMP\laragon.exe"
    Invoke-WebRequest -Uri "https://github.com/leokhoa/laragon/releases/latest/download/laragon.exe" -OutFile $installer
    Start-Process -FilePath $installer -ArgumentList "/silent" -Wait
    $LaragonBin = "$env:ProgramFiles\Laragon\usr\bin"
    if (-not (Test-Path $LaragonBin)) {
        $LaragonBin = "C:\Laragon\usr\bin"
    }
    Write-Success "Laragon installed."
}

# ---------------------------------------------------------------
# 2. Start MySQL via Laragon
# ---------------------------------------------------------------
Write-Step "2. Starting MySQL service"

$laragonExe = Resolve-Path "$LaragonBin\..\..\laragon.exe" -ErrorAction SilentlyContinue
if ($laragonExe) {
    Start-Process -FilePath $laragonExe -ArgumentList "start mysql" -NoNewWindow -Wait
    Write-Success "MySQL started via Laragon."
} else {
    Write-Warn "Cannot find laragon.exe. Attempting to start mysqld directly..."
    $mysqld = "$LaragonBin\mysqld.exe"
    if (Test-Path $mysqld) {
        Start-Process -FilePath $mysqld -NoNewWindow
    }
}

Write-Info "Waiting for MySQL to become available..."
$maxRetries = 30
$connected = $false
for ($i = 0; $i -lt $maxRetries; $i++) {
    try {
        $conn = [System.Data.SqlClient.SqlConnection]::new()
        $conn.ConnectionString = "server=127.0.0.1;port=3306;uid=$DB_USER;pwd=$DB_PASS"
        $conn.Open()
        $conn.Close()
        $connected = $true
        break
    } catch {
        Start-Sleep -Seconds 2
    }
}

if (-not $connected) {
    Write-Warn "Could not connect to MySQL after 60s. Check if MySQL is running on port 3306."
} else {
    Write-Success "MySQL is running."
}

# ---------------------------------------------------------------
# 3. Create database
# ---------------------------------------------------------------
Write-Step "3. Creating database '$DB_NAME'"

$env:Path = "$LaragonBin;$env:Path"
$mysql = "$LaragonBin\mysql.exe"

$dbExists = & $mysql -u$DB_USER -e "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '$DB_NAME'" --skip-column-names 2>$null
if ($dbExists) {
    Write-Info "Database '$DB_NAME' already exists. Dropping and recreating..."
    & $mysql -u$DB_USER -e "DROP DATABASE IF EXISTS $DB_NAME"
}

& $mysql -u$DB_USER -e "CREATE DATABASE $DB_NAME DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci"
Write-Success "Database '$DB_NAME' created."

# ---------------------------------------------------------------
# 4. Run schema files
# ---------------------------------------------------------------
Write-Step "4. Running SQL schema files"

$schemaDir = "$ProjectRoot\src\main\java\schemas"
if (Test-Path $schemaDir) {
    $sqlFiles = Get-ChildItem -Path $schemaDir -Filter "*.sql" | Sort-Object Name
    foreach ($file in $sqlFiles) {
        Write-Info "  Running $($file.Name)..."
        & $mysql -u$DB_USER $DB_NAME -e "source $($file.FullName)" 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Success "  $($file.Name) done."
        } else {
            Write-Warn "  $($file.Name) may have errors (table might already exist via Hibernate)."
        }
    }
} else {
    Write-Warn "Schema directory not found at $schemaDir. Skipping."
}

Write-Success "Database setup complete."

# ---------------------------------------------------------------
# 5. Check Java & Maven
# ---------------------------------------------------------------
Write-Step "5. Checking prerequisites"

$javaOk = $false
try {
    $javaVer = java -version 2>&1
    if ($javaVer -match '"(\d+)"') {
        $v = $Matches[1]
        if ([int]$v -ge 21) { $javaOk = $true }
    } elseif ($javaVer -match "version ""(\d+)") {
        $v = $Matches[1]
        if ([int]$v -ge 21) { $javaOk = $true }
    }
} catch {}

if (-not $javaOk) {
    Write-Warn "Java 21+ not found. Please install JDK 21 from https://adoptium.net/"
} else {
    Write-Success "Java 21+ detected."
}

$mvnOk = $false
try {
    $mvnVer = mvn --version 2>&1
    if ($mvnVer -match "Apache Maven") { $mvnOk = $true }
} catch {}

if (-not $mvnOk) {
    Write-Warn "Maven not found. Please install Maven from https://maven.apache.org/download.cgi"
} else {
    Write-Success "Maven detected."
}

# ---------------------------------------------------------------
# 6. Build project
# ---------------------------------------------------------------
if (-not $SkipBuild) {
    Write-Step "6. Building project with Maven"
    Set-Location -Path $ProjectRoot
    mvn clean package -DskipTests
    if ($LASTEXITCODE -eq 0) {
        Write-Success "Build successful!"
    } else {
        Write-Error "Build failed. Check the output above."
    }
} else {
    Write-Step "6. Skipping build (use -SkipBuild if you want to skip)"
}

# ---------------------------------------------------------------
# 7. Done
# ---------------------------------------------------------------
Write-Step "7. Setup complete!"

$jarFiles = Get-ChildItem -Path "$ProjectRoot\target" -Filter "*.jar" | Sort-Object LastWriteTime -Descending
if ($jarFiles) {
    $jar = $jarFiles[0].FullName
    Write-Success "Packaged JAR: $jar"
    Write-Host "`nTo run the application:"
    Write-Host "  java -jar `"$jar`"" -ForegroundColor Green
} else {
    Write-Host "`nTo run the application from your IDE, open the project and run App.java" -ForegroundColor Yellow
}

Write-Host "`nMake sure your hibernate.cfg.xml points to localhost:3306/StoreManagement with root/no password." -ForegroundColor Gray
Write-Host "If you changed MySQL credentials, edit: src/main/resources/hanabi/backend/hibernate.cfg.xml" -ForegroundColor Gray
