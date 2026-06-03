param(
    [switch]$SkipTests
)

$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

function Write-Step($msg) {
    Write-Host "`n==> $msg" -ForegroundColor Cyan
}

function Write-Success($msg) {
    Write-Host "  $msg" -ForegroundColor Green
}

function Write-Warn($msg) {
    Write-Host "  $msg" -ForegroundColor Yellow
}

# ---------------------------------------------------------------
# 1. Check Java & Maven
# ---------------------------------------------------------------
Write-Step "1. Checking prerequisites"

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
    Write-Host "  Java 21+ is required. Install from https://adoptium.net/" -ForegroundColor Red
    exit 1
}
Write-Success "Java 21+ detected."

$mvnOk = $false
try {
    $mvnVer = mvn --version 2>&1
    if ($mvnVer -match "Apache Maven") { $mvnOk = $true }
} catch {}

if (-not $mvnOk) {
    Write-Host "  Maven is required. Install from https://maven.apache.org/download.cgi" -ForegroundColor Red
    exit 1
}
Write-Success "Maven detected."

# ---------------------------------------------------------------
# 2. Convert PNG to ICO (using Java if ImageMagick not available)
# ---------------------------------------------------------------
Write-Step "2. Preparing application icon"

$icoPath = "$ProjectRoot\src\main\resources\hanabi\assets\icon\HanabiCafe.ico"
$pngPath = "$ProjectRoot\src\main\resources\hanabi\assets\icon\HanabiCafe.png"

if (-not (Test-Path $icoPath)) {
    if (Test-Path $pngPath) {
        Write-Warn "No .ico file found. Building without custom icon."
        Write-Warn "To add an icon, convert HanabiCafe.png to HanabiCafe.ico using https://convertio.co/ or GIMP."
    } else {
        Write-Warn "No icon files found. Building without custom icon."
    }
}

# ---------------------------------------------------------------
# 3. Build fat JAR with shade plugin
# ---------------------------------------------------------------
Write-Step "3. Building fat JAR (shade)"

Set-Location -Path $ProjectRoot
$skipTestsArg = if ($SkipTests) { "-DskipTests" } else { "" }

Invoke-Expression "mvn clean package $skipTestsArg -q"
if ($LASTEXITCODE -ne 0) {
    Write-Host "  Maven build failed!" -ForegroundColor Red
    exit 1
}
Write-Success "Fat JAR built successfully."

# ---------------------------------------------------------------
# 4. Create EXE with Launch4j
# ---------------------------------------------------------------
Write-Step "4. Creating EXE with Launch4j"

Invoke-Expression "mvn launch4j:launch4j -q"
if ($LASTEXITCODE -ne 0) {
    Write-Host "  Launch4j failed. Trying with verbose output..." -ForegroundColor Yellow
    Invoke-Expression "mvn launch4j:launch4j"
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  Launch4j EXE creation failed!" -ForegroundColor Red
        exit 1
    }
}

# ---------------------------------------------------------------
# 5. Done
# ---------------------------------------------------------------
$exePath = "$ProjectRoot\target\HanabiCafe.exe"
$jarPath = "$ProjectRoot\target\*.jar"

Write-Step "5. Build complete!"

if (Test-Path $exePath) {
    Write-Success "EXE created: $exePath"
    Write-Host ""
    Write-Host "  To distribute:" -ForegroundColor Cyan
    Write-Host "    1. Copy HanabiCafe.exe to the target machine" -ForegroundColor Gray
    Write-Host "    2. Place a 'jre' folder next to it with a JRE 21+ runtime" -ForegroundColor Gray
    Write-Host "       (or ensure Java 21+ is installed on the target machine)" -ForegroundColor Gray
    Write-Host ""
    Write-Host "  To run:" -ForegroundColor Cyan
    Write-Host "    .\target\HanabiCafe.exe" -ForegroundColor Green
} else {
    Write-Warn "EXE not found at expected location."
    Write-Host "  Check target/ directory for output files." -ForegroundColor Gray
}

Write-Host ""
Write-Host "  Fat JAR is also available in target/ for direct execution:" -ForegroundColor Gray
Write-Host "    java -jar target\DACN-1-1.0-SNAPSHOT.jar" -ForegroundColor Green
