# DNCS_one (HANABI CAFE) — Code Review

**Project:** Coffee shop management system (Java Swing + Hibernate + MySQL)  
**Files analyzed:** 45 Java source files + configuration  
**Review date:** 2026-05-25

---

## 🔴 CRITICAL / HIGH SEVERITY

### H1. Plain-text password storage
**Files:** `StaffDAO.java:47`, `AuthService.java`, `DataInitalizer.java`, `AccountService.java:93`

Passwords are stored and compared in plaintext with no hashing.

```java
// StaffDAO.java
"FROM Staff WHERE staffName = :name AND password = :pass"
```

Default password `admin` is hardcoded in `DataInitalizer.java`.  
**Fix:** Use bcrypt/argon2/PBKDF2 via a library (e.g., Spring Security Crypto or jBCrypt).

---

### H2. `MenuService.placeOrder()` — Order.invoice FK never set → data corruption
**File:** `MenuService.java:44–64`

The `Order` entity has a `@ManyToOne` → `Invoice` (mapped to column `invoice_id`), but `order.setInvoice(invoice)` is **never called**. The invoice FK column in the `orders` table will remain `NULL` for every order.

The `Invoice` is saved after the `Order`, but the relationship is bidirectional only on the Java side and is never wired up.

**Fix:** Add `order.setInvoice(invoice)` before saving the order (or change save order so the invoice is persisted first).

---

### H3. `MenuService.placeOrder()` — wrong DAO lookup → items silently lost
**File:** `MenuService.java:69`

```java
Product product = productDAO.findByCategory(productName).stream().findFirst().orElse(null);
```

`findByCategory(productName)` searches products **by category** using the product name string. This returns empty for most lookups, and the `if (product != null)` guard silently skips the order detail instead of reporting the error.

**Fix:** Add a `ProductDAO.findByName(String)` method and call that instead.

---

### H4. `AddProductForm` — NumberFormatException on price/cost parse
**File:** `AddProductForm.java:187–191`

```java
if (!priceStr.isEmpty()) {
    p.setPrice(Double.parseDouble(priceStr));  // crash if invalid
}
if (!costStr.isEmpty()) {
    p.setCost(Double.parseDouble(costStr));    // crash if invalid
}
```

`Double.parseDouble` on unchecked user input can throw `NumberFormatException`. The catch block on line 203 only catches exceptions from a different scope.

**Fix:** Wrap in try-catch or validate with regex before parsing.

---

### H5. Password field uses `JTextField` — plain text visible
**File:** `CreateUser.java:21,61`

```java
private JTextField txtPassword;
// …
txtPassword = new JTextField("123456");
```

The default password `"123456"` is visible in plain text. Should use `JPasswordField` and a non-trivial placeholder.

---

### H6. Empty MySQL root password
**Files:** `global.java:13`, `hibernate.cfg.xml:11`

```java
public static final String PASSWORD = "";
```

The database connection has no password. Both the Java constant and Hibernate XML config use empty credentials. This is a security risk if the database is accessible from the network.

---

### H7. `AccountService.getPoints()` — double database call
**File:** `AccountService.java:38–40`

```java
return averageDAO.findById(staffId) != null
    ? averageDAO.findById(staffId).getAverageScore()
    : 0;
```

`findById(staffId)` is called twice. Should store the result in a local variable.

---

### H8. `RevenuePanel.CustomChartPanel` — data array length mismatch → crash risk
**File:** `RevenuePanel.java:450–451`

```java
private final double[] dataPoints = {0.2, 0.3, … 0.9};  // 19 elements
private final String[] xLabels = {"1", "3", … "31"};     // 16 elements
```

`dataPoints.length` (19) and `xLabels.length` (16) differ. The chart iterates over `dataPoints.length` for coordinates but uses `xLabels.length` for X-axis labels. The mismatch between `dataStepX` and `stepX` calculations can cause misaligned rendering. Both should draw from the same source.

---

### H9. `RevenuePanel` — mock data never replaced by live data
**Files:** `RevenuePanel.java:325–329, 372–377, 450–451`

- Bottom section (`createBottomSection()`) renders hardcoded order/product data; `loadData()` replaces it.
- The custom chart (`CustomChartPanel`) is **always** hardcoded — never updated from `RevenueService`.
- Filter button actions on the chart do nothing except toggle styles.

**Fix:** Drive chart and bottom section from actual `RevenueService` calls.

---

### H10. `PopUp.initComponent()` — null parent reference
**File:** `PopUp.java:41–47`

```java
public PopUp(JComponent Main, String title, int width, int height) {
    this.main = Main;        // parent is NEVER set
    // …
}
public void initComponent() {
    setLocationRelativeTo(parent);  // parent is null
```

The constructor saves `Main` into `this.main` but never assigns `this.parent`. Calling `initComponent()` passes null to `setLocationRelativeTo()`.

---

## 🟡 MEDIUM SEVERITY

| #  | Issue | File(s) | Details |
|----|-------|---------|---------|
| M1 | No `@Transactional` on DAO read operations | All `*DAO.java` | Read-only Hibernate operations may throw `LazyInitializationException` when lazy associations are accessed outside a session. Add `@Transactional(readOnly = true)` or wrap in `session.beginTransaction()`. |
| M2 | `CreateUser.form()` — unguarded `Double.parseDouble` | `CreateUser.java:134` | `Double.parseDouble(txtSalary.getText())` throws `NumberFormatException` if salary is empty or non-numeric. Only `HeadlessException` is caught. |
| M3 | Empty `LoginView.java` | `LoginView.java` | The class body is completely empty — dead code. |
| M4 | `SalaryPanel` dead code | `DashboardView.java:24,49,54` | `SalaryPanel` is instantiated but commented out everywhere. The class exists but is never used. |
| M5 | Filesystem fallback in `loadProductImage()` not portable | `MenuItemsPanel.java:307–325` | The fallback resolves `user.dir` + `/src/main/resources/` — works in IDE but fails in production JAR. |
| M6 | Image copy to `/target/classes/` is fragile | `AddProductForm.java:171–179` | `mvn clean` deletes `target/`. Copying runtime files here is not reliable. |
| M7 | Method name mismatch: `ChangePassword` vs `changePassword` | `AccountPanel.java:325`, `AccountService.java:93` | The UI calls `ChangePassword(newPass)` (capital C) but `AccountService` defines `changePassword(...)` (lowercase c). Works only because Java method names are case-sensitive and the call at line 325 correctly matches the definition at line 420. |
| M8 | Typo: table name `salarys` | `TableDBInitalizer.java`, `Salary.java` | SQL table is named `salarys` instead of `salaries`. |
| M9 | Typo: `Initalizer` → `Initializer` | `DataInitalizer.java`, `TableDBInitalizer.java`, `DBInitalizer.java` | All three utility files misspell "Initializer". |
| M10 | Mutable static field `assetsPath` | `global.java:8` | `public static String assetsPath` can be modified from anywhere — thread-unsafe. |
| M11 | `RevenueService` — unused import | `RevenueService.java:7,21` | `ProductDAO` is imported but never used. |
| M12 | `MainForm.showForm()` may add component to wrong parent | `MainForm.java:35–39` | No check that the component isn't already attached to another container. |
| M13 | `MenuItemsPanel` — no confirmation before payment | `MenuItemsPanel.java:470–478` | `Proceed to Payment` button has no action listener attached; the `payBtn` is styled but does nothing. |

---

## 🟢 LOW SEVERITY / CODE QUALITY

| #  | Issue | Details |
|----|-------|---------|
| L1 | Unused imports | `CreateUser.java:3` (`Checkbox`, `FlatSVGIcon`), `RevenuePanel.java:47` (duplicate `Box`) |
| L2 | Extensive commented-out code | `DashboardView.java` (lines 19, 24, 33, 49, 54, 84, 89–102), `CategoryPanel.java` (33, 42, 49, 91, 96, 141, 204–208), `RevenuePanel.java` (683–696), `AccountPanel.java` (46–61) |
| L3 | `ButtonLink` component unused | `ButtonLink.java` exists in the components package but has zero references across the project. |
| L4 | Maven targets Java 25 | `pom.xml:12–13` sets source/target to 25. Most environments and CI runners do not have JDK 25 installed. Consider LTS versions (17 or 21). |
| L5 | Mixed naming conventions in `salarys` table | Column `baseSalary` (camelCase) vs `CommissionRate` (PascalCase) in the same DDL. |
| L6 | `SalaryDAO.findAllWithStaffAndTotals()` uses the wrong formula for total | `"COALESCE(sa.baseSalary, 0) + COALESCE(sa.commissionRate, 0)"` — this adds `commissionRate` (a percentage/rate) directly to `baseSalary` (an absolute amount). Mathematically wrong if `commissionRate` is a percentage. |
| L7 | No test directory | `src/test/` exists but is completely empty — zero unit or integration tests. |
| L8 | `AccountPanel.loadUser()` — admin check style | `u.isAdmin() == true` is redundant; `u.isAdmin()` is sufficient. |
| L9 | `Banner` uses dancing script font for Vietnamese text | `DancingScript-Regular.ttf` is a cursive English font — not suitable for Vietnamese text readability. |

---

## 📊 Summary

| Severity | Count | Key Concerns |
|----------|-------|-------------|
| **🔴 Critical/High** | **10** | Plaintext passwords, unset FK relationships, wrong DAO lookup, crash on parse, visible password field, empty DB password, double DB calls, chart crash risk, mock data never replaced, null reference in PopUp |
| **🟡 Medium** | **13** | Transaction boundaries, exception handling, dead code, portability, fragile build paths, naming issues, unused code |
| **🟢 Low** | **9** | Code style, unused imports, commented-out code, Java version, SQL formula |
| **Total** | **32** | |

### Top 5 recommended fixes (in order of impact)

1. **Hash passwords** — add bcrypt/argon2 to `StaffDAO.authenticate()` and `AccountService.changePassword()`
2. **Fix `MenuService.placeOrder()`** — link `Order` ↔ `Invoice`, fix product lookup, add error handling
3. **Fix `AccountService.getPoints()`** — cache the DAO result instead of two calls
4. **Fix hardcoded chart data** — wire `RevenuePanel.CustomChartPanel` to real data
5. **Add input validation** — guard all `Double.parseDouble` calls in `AddProductForm` and `CreateUser`
