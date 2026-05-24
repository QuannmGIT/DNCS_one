package hanabi.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class SalaryTablePanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color TEXT_MENU = new Color(74, 53, 44);

    private DefaultTableModel salaryModel;
    private JLabel countLabel;

    public SalaryTablePanel() {
        initComponents();
    }

    public void loadSalaryData(java.util.List<Object[]> salaryData) {
        salaryModel.setRowCount(0);
        for (int i = 0; i < salaryData.size(); i++) {
            Object[] row = salaryData.get(i);
            salaryModel.addRow(new Object[]{
                    i + 1,
                    row[1] != null ? row[1] : "",
                    row[2] != null ? row[2] : "",
                    row[3] != null ? String.format("%,.0f\u0111", row[3]).replace(",", ".") : "0\u0111",
                    row[4] != null ? String.format("%,.0f", row[4]).replace(",", ".") : "0",
                    row[5] != null ? String.format("%,.0f\u0111", row[5]).replace(",", ".") : "0\u0111"
            });
        }
        countLabel.setText(salaryData.size() + " Staff");
    }

    public void clearData() {
        salaryModel.setRowCount(0);
        countLabel.setText("0 Staff");
    }

    private void initComponents() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 10));

        add(createTitleRow(), BorderLayout.NORTH);
        add(createScrollPane(), BorderLayout.CENTER);
    }

    private JPanel createTitleRow() {
        JLabel title = new JLabel("Salary table");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(DARK_BROWN);

        countLabel = new JLabel("0 staff");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLabel.setForeground(new Color(160, 140, 125));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(countLabel, BorderLayout.EAST);
        return titleRow;
    }

    private JScrollPane createScrollPane() {
        String[] cols = {"STT", "Name", "Role", "Salary", "Commission rate", "Total salary"};

        salaryModel = new DefaultTableModel(new Object[][]{}, cols) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(salaryModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(34);
        table.setGridColor(new Color(230, 220, 210));
        table.setBackground(Color.WHITE);
        table.setForeground(TEXT_MENU);
        table.setSelectionBackground(new Color(191, 161, 127));
        table.setSelectionForeground(Color.WHITE);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(Color.WHITE);
        header.setBackground(DARK_BROWN);
        header.setPreferredSize(new Dimension(0, 40));
        header.setResizingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i >= 3) {
                table.getColumnModel().getColumn(i).setCellRenderer(new StripeRightRenderer());
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(new StripeCenterRenderer());
            }
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 195, 180), 1),
                BorderFactory.createEmptyBorder(2, 0, 2, 0)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setOpaque(false);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JPanel() {
            {
                setBackground(DARK_BROWN);
            }
        });
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        return scroll;
    }

    private static class StripeCenterRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(252, 250, 248));
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    private static class StripeRightRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(252, 250, 248));
            }
            setHorizontalAlignment(SwingConstants.RIGHT);
            return c;
        }
    }
}
