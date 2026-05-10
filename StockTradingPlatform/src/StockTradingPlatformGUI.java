import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

class PortfolioItem {
    Stock stock;
    int quantity;

    PortfolioItem(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    double getValue() {
        return stock.price * quantity;
    }
}

public class StockTradingPlatformGUI extends JFrame {

    ArrayList<Stock> marketStocks = new ArrayList<>();
    HashMap<String, PortfolioItem> portfolio = new HashMap<>();

    DefaultTableModel marketModel, portfolioModel, transactionModel;
    JTable marketTable, portfolioTable, transactionTable;

    JLabel balanceLabel, valueLabel, totalLabel;
    double balance = 100000;

    public StockTradingPlatformGUI() {
        setTitle("Stock Trading Platform");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addSampleStocks();

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Market", createMarketPanel());
        tabs.add("Portfolio", createPortfolioPanel());
        tabs.add("Transactions", createTransactionPanel());

        add(tabs);

        setVisible(true);
    }

    private void addSampleStocks() {
        marketStocks.add(new Stock("TCS", "Tata Consultancy Services", 3850));
        marketStocks.add(new Stock("INFY", "Infosys", 1450));
        marketStocks.add(new Stock("RELIANCE", "Reliance Industries", 2950));
        marketStocks.add(new Stock("HDFC", "HDFC Bank", 1600));
        marketStocks.add(new Stock("WIPRO", "Wipro", 520));
    }

    private JPanel createMarketPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        marketModel = new DefaultTableModel(new String[]{"Symbol", "Company", "Price"}, 0);
        marketTable = new JTable(marketModel);

        for (Stock s : marketStocks) {
            marketModel.addRow(new Object[]{s.symbol, s.name, s.price});
        }

        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        balanceLabel = new JLabel("Balance: ₹" + balance, JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(balanceLabel);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(marketTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton buyButton = new JButton("Buy Stock");
        JButton sellButton = new JButton("Sell Stock");

        buttonPanel.add(buyButton);
        buttonPanel.add(sellButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        buyButton.addActionListener(e -> buyStock());
        sellButton.addActionListener(e -> sellStock());

        return panel;
    }

    private JPanel createPortfolioPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        portfolioModel = new DefaultTableModel(
                new String[]{"Symbol", "Company", "Quantity", "Price", "Total Value"}, 0
        );

        portfolioTable = new JTable(portfolioModel);
        panel.add(new JScrollPane(portfolioTable), BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(1, 2));

        valueLabel = new JLabel("Portfolio Value: ₹0", JLabel.CENTER);
        totalLabel = new JLabel("Total Net Worth: ₹" + balance, JLabel.CENTER);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        summaryPanel.add(valueLabel);
        summaryPanel.add(totalLabel);

        panel.add(summaryPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        transactionModel = new DefaultTableModel(
                new String[]{"Type", "Symbol", "Quantity", "Price", "Total Amount"}, 0
        );

        transactionTable = new JTable(transactionModel);
        panel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        return panel;
    }

    private void buyStock() {
        int row = marketTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a stock to buy.");
            return;
        }

        String symbol = marketModel.getValueAt(row, 0).toString();
        Stock selectedStock = findStock(symbol);

        String qtyText = JOptionPane.showInputDialog(this, "Enter quantity to buy:");

        try {
            int qty = Integer.parseInt(qtyText);

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                return;
            }

            double cost = selectedStock.price * qty;

            if (cost > balance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance.");
                return;
            }

            balance -= cost;

            if (portfolio.containsKey(symbol)) {
                portfolio.get(symbol).quantity += qty;
            } else {
                portfolio.put(symbol, new PortfolioItem(selectedStock, qty));
            }

            transactionModel.addRow(new Object[]{"BUY", symbol, qty, selectedStock.price, cost});

            updatePortfolio();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity.");
        }
    }

    private void sellStock() {
        int row = marketTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a stock to sell.");
            return;
        }

        String symbol = marketModel.getValueAt(row, 0).toString();

        if (!portfolio.containsKey(symbol)) {
            JOptionPane.showMessageDialog(this, "You do not own this stock.");
            return;
        }

        PortfolioItem item = portfolio.get(symbol);

        String qtyText = JOptionPane.showInputDialog(this, "Enter quantity to sell:");

        try {
            int qty = Integer.parseInt(qtyText);

            if (qty <= 0 || qty > item.quantity) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }

            double amount = item.stock.price * qty;
            balance += amount;
            item.quantity -= qty;

            if (item.quantity == 0) {
                portfolio.remove(symbol);
            }

            transactionModel.addRow(new Object[]{"SELL", symbol, qty, item.stock.price, amount});

            updatePortfolio();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity.");
        }
    }

    private Stock findStock(String symbol) {
        for (Stock s : marketStocks) {
            if (s.symbol.equals(symbol)) {
                return s;
            }
        }
        return null;
    }

    private void updatePortfolio() {
        portfolioModel.setRowCount(0);

        double portfolioValue = 0;

        for (PortfolioItem item : portfolio.values()) {
            double value = item.getValue();
            portfolioValue += value;

            portfolioModel.addRow(new Object[]{
                    item.stock.symbol,
                    item.stock.name,
                    item.quantity,
                    item.stock.price,
                    value
            });
        }

        balanceLabel.setText("Balance: ₹" + String.format("%.2f", balance));
        valueLabel.setText("Portfolio Value: ₹" + String.format("%.2f", portfolioValue));
        totalLabel.setText("Total Net Worth: ₹" + String.format("%.2f", balance + portfolioValue));
    }

    public static void main(String[] args) {
        new StockTradingPlatformGUI();
    }
}