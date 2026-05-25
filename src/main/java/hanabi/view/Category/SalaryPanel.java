package hanabi.view.Category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import hanabi.components.SalaryTablePanel;
import hanabi.service.AccountService;

public class SalaryPanel extends JPanel {

    public static final int PANEL_WIDTH = 900;
    public static final int PANEL_HEIGHT = 700;

    private final AccountService accountService = new AccountService();
    private SalaryTablePanel salaryTablePanel;

    public SalaryPanel() {
        initComponents();
    }

    public void loadData() {
        salaryTablePanel.clearData();

        new SwingWorker<Void, Void>() {
            private java.util.List<Object[]> salaryData;

            @Override
            protected Void doInBackground() {
                salaryData = accountService.getSalaryData();
                return null;
            }

            @Override
            protected void done() {
                salaryTablePanel.loadSalaryData(salaryData);
            }
        }.execute();
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new BorderLayout());

        salaryTablePanel = new SalaryTablePanel();
        add(salaryTablePanel, BorderLayout.CENTER);
    }
}
