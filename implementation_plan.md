# Background Thread Implementation Plan

Implement `SwingWorker` for heavy database queries in the UI to prevent the Event Dispatch Thread (EDT) from freezing.

## User Review Required

Please review the proposed files to modify. Does this approach look good to proceed? 

## Proposed Changes

### `hanabi/view/Category/RevenuePanel.java`
- Modify `loadData()`: Wrap data fetching (`getTodayRevenue`, `getTotalOrders`, `getBestSeller`, `getAverageRating`, `getRecentOrders`, `getTopSellingProducts`) inside a `SwingWorker`'s `doInBackground()`. Update labels and tables in `done()`.
- Modify `loadChartData(String filter)`: Wrap `getRevenueByDateRange` and `getMonthlyRevenue` inside a `SwingWorker`. Update `chartPanel` in `done()`.

### `hanabi/view/Category/AccountPanel.java`
- Modify `loadUser()`: Wrap `getTotalOrders`, `getPoints`, and `getSalaryData` inside a `SwingWorker`'s `doInBackground()`. Update the UI in `done()`.

### `hanabi/view/Category/SalaryPanel.java`
- Modify `loadData()`: Wrap `accountService.getSalaryData()` inside a `SwingWorker`. Update the table in `done()`.

### `hanabi/view/Category/MenuItemsPanel.java`
- Modify `loadMenuItems()`: Wrap `menuService.getAvailableProducts()` inside a `SwingWorker`. Process the products list in `doInBackground()` and call `rebuildGrid()` in `done()`.

## Verification Plan
After making the changes, I will visually verify that the code compiles without syntax errors and that the UI logic correctly separates background processing from EDT UI updates.
