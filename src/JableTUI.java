import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * <h1>Java Table Text User Interface (JableTUI)</h1>
 * <p>
 * A simple table text user interface for Java
 * </p>
 *
 * @author <a href="https://github.com/nowl-it">NOwL</a>
 * @version 0.0.1beta
 * @see <a href="https://github.com/nowl-it/jabletui">JableTUI</a>
 * @since 2024-11-20
 */
public class JableTUI {
    private final Scanner scanner = new Scanner(System.in);
    protected String[] menu = {};
    protected int tableMenuSelect;
    private int tableCurrentPage = 1;
    private boolean noTableMenuShow = false;
    private List<Map<String, String>> tableData;
    private List<Map<String, String>> tableCurrentRecords;
    private List<String> tableColumns;
    private int tablePageSize;

    /**
     * Register table data
     *
     * @param data     List of values to be displayed
     * @param pageSize Number of items to be displayed per page
     */
    protected void tableRegister(List<Map<String, String>> data, int pageSize) {
        if (data.isEmpty()) {
            System.err.println("No records found");
            return;
        }

        this.tablePageSize = pageSize;
        this.tableData = data;

        this.tableCurrentRecords = tableData.subList(
                Math.min(this.tablePageSize * this.tableCurrentPage - this.tablePageSize, this.tableData.size()),
                Math.min(this.tablePageSize * this.tableCurrentPage, this.tableData.size()));

        this.tableColumns = new ArrayList<>(tableData.getFirst().keySet());
    }

    /**
     * Disable table menu
     */
    protected void noMenu() {
        this.noTableMenuShow = true;
    }

    /**
     * Print table
     */
    protected void print() {
        do {
            this.printHeader();
            this.printRow(this.tableColumns);
            this.printHeader();

            for (Map<String, String> record : this.tableCurrentRecords) {
                this.printRow(record);
                this.printHeader();
            }

            this.printInfo();

            if (!this.noTableMenuShow) {
                try {
                    this.tableMenu();
                } catch (Exception e) {
                    System.out.println("Invalid input, auto exit");
                }

                switch (this.tableMenuSelect) {
                    case 1 -> firstPage();
                    case 2 -> previousPage();
                    case 3 -> nextPage();
                    case 4 -> lastPage();
                    case 0 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid input, please try again");
                }
            }
            this.noTableMenuShow = false;
        } while (this.tableMenuSelect != 0);
    }

    /**
     * Custom table menu
     *
     * @param menu List of custom menu
     */
    protected void tableCustomMenu(String[] menu) {
        this.menu = menu;
    }

    private String getLongestStringColumn(int i) {
        String longestStringColumn = this.tableColumns.get(i);
        for (Map<String, String> record : this.tableCurrentRecords) {
            if (longestStringColumn.length() < record.get(this.tableColumns.get(i)).length()) {
                longestStringColumn = record.get(this.tableColumns.get(i));
            }
        }

        return longestStringColumn;
    }

    private void printHeader() {
        for (int i = 0; i < this.tableColumns.size(); i++) {
            String longestStringColumn = getLongestStringColumn(i);
            System.out.print("+" + "-".repeat(longestStringColumn.length() + 2));
            if (i == this.tableColumns.size() - 1) {
                System.out.println("+");
            }
        }
    }

    private void printRow(List<String> columns) {
        for (int i = 0; i < columns.size(); i++) {
            String longestStringColumn = getLongestStringColumn(i);
            int totalSpaces = longestStringColumn.length() - columns.get(i).length() + 2;
            System.out.print("|" + " ".repeat(totalSpaces / 2) + columns.get(i)
                    + " ".repeat(totalSpaces - (totalSpaces / 2)));
            if (i == columns.size() - 1) {
                System.out.println("|");
            }
        }
    }

    private void printRow(Map<String, String> record) {
        for (int i = 0; i < this.tableColumns.size(); i++) {
            String longestStringColumn = getLongestStringColumn(i);
            String value = record.get(this.tableColumns.get(i));
            int totalSpaces = longestStringColumn.length() - value.length() + 2;
            System.out.print("|" + " ".repeat(totalSpaces / 2) + value
                    + " ".repeat(totalSpaces - (totalSpaces / 2)));
            if (i == this.tableColumns.size() - 1) {
                System.out.println("|");
            }
        }
    }

    private void printInfo() {
        System.out.println("Page " + this.tableCurrentPage + " / "
                + (int) Math.ceil((double) this.tableData.size() / this.tablePageSize));
    }

    private void firstPage() {
        if (this.tableData.size() > this.tablePageSize) {
            this.tableCurrentPage = 1;
            this.updateCurrentTableRecords();
        }
    }

    private void previousPage() {
        if (this.tableCurrentPage > 1 && this.tableData.size() > this.tablePageSize) {
            this.tableCurrentPage--;
            this.updateCurrentTableRecords();
        }
    }

    private void nextPage() {
        if (this.tableCurrentPage < (int) Math.ceil((double) this.tableData.size() / this.tablePageSize)
                && this.tableData.size() > this.tablePageSize) {
            this.tableCurrentPage++;
            this.updateCurrentTableRecords();
        }
    }

    private void lastPage() {
        if (this.tableData.size() > this.tablePageSize) {
            this.tableCurrentPage = (int) Math.ceil((double) this.tableData.size() / this.tablePageSize);
            this.updateCurrentTableRecords();
        }
    }

    private void updateCurrentTableRecords() {
        this.tableCurrentRecords = this.tableData.subList(
                Math.min(this.tablePageSize * this.tableCurrentPage - this.tablePageSize, this.tableData.size()),
                Math.min(this.tablePageSize * this.tableCurrentPage, this.tableData.size()));
    }

    private void tableMenu() {
        if (this.tableData.size() > this.tablePageSize) {
            System.out.println("1. Previous page");
            System.out.println("2. Next page");
            System.out.println("3. First page");
            System.out.println("4. Last page");
            System.out.println("0. Exit");
            System.out.print("Select: ");
            this.tableMenuSelect = scanner.nextInt();
        } else {
            System.out.println("No more pages, auto exit");
            this.tableMenuSelect = 0;
        }

    }
}
