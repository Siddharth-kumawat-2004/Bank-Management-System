import java.io.*;
import java.util.*;

public class BankManagementSystem {

    private static final String FILE_NAME = "accounts.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Bank Management System ===");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Balance Inquiry");
            System.out.println("5. Update Account Details");
            System.out.println("6. Delete Account");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    depositMoney(scanner);
                    break;
                case 3:
                    withdrawMoney(scanner);
                    break;
                case 4:
                    balanceInquiry(scanner);
                    break;
                case 5:
                    updateAccount(scanner);
                    break;
                case 6:
                    deleteAccount(scanner);
                    break;
                case 7:
                    System.out.println("Thank you for using the Bank Management System.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }

    private static void createAccount(Scanner scanner) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            System.out.print("Enter Name: ");
            String name = scanner.next();
            System.out.print("Enter Account Number: ");
            long accountNumber = scanner.nextLong();
            System.out.print("Enter Age: ");
            int age = scanner.nextInt();
            System.out.print("Enter Initial Balance: ");
            double balance = scanner.nextDouble();

            String record = name + "," + accountNumber + "," + age + "," + balance;
            writer.write(record);
            writer.newLine();

            System.out.println("Account created successfully!");
        } catch (IOException e) {
            System.err.println("Error creating account: " + e.getMessage());
        }
    }

    private static void depositMoney(Scanner scanner) {
        List<String> accounts = readAccounts();
        System.out.print("Enter Account Number: ");
        long accountNumber = scanner.nextLong();
        System.out.print("Enter Deposit Amount: ");
        double amount = scanner.nextDouble();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String account : accounts) {
                String[] details = account.split(",");
                long accNum = Long.parseLong(details[1]);
                if (accNum == accountNumber) {
                    double balance = Double.parseDouble(details[3]) + amount;
                    writer.write(details[0] + "," + accNum + "," + details[2] + "," + balance);
                    found = true;
                } else {
                    writer.write(account);
                }
                writer.newLine();
            }
            if (found) {
                System.out.println("Deposit successful!");
            } else {
                System.out.println("Account not found.");
            }
        } catch (IOException e) {
            System.err.println("Error processing deposit: " + e.getMessage());
        }
    }

    private static void withdrawMoney(Scanner scanner) {
        List<String> accounts = readAccounts();
        System.out.print("Enter Account Number: ");
        long accountNumber = scanner.nextLong();
        System.out.print("Enter Withdrawal Amount: ");
        double amount = scanner.nextDouble();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String account : accounts) {
                String[] details = account.split(",");
                long accNum = Long.parseLong(details[1]);
                if (accNum == accountNumber) {
                    double balance = Double.parseDouble(details[3]);
                    if (balance - amount >= 1000) {
                        balance -= amount;
                        writer.write(details[0] + "," + accNum + "," + details[2] + "," + balance);
                        found = true;
                    } else {
                        System.out.println("Insufficient balance. Minimum balance of ₹1000 must be maintained.");
                        writer.write(account);
                    }
                } else {
                    writer.write(account);
                }
                writer.newLine();
            }
            if (found) {
                System.out.println("Withdrawal successful!");
            } else if (!found) {
                System.out.println("Account not found.");
            }
        } catch (IOException e) {
            System.err.println("Error processing withdrawal: " + e.getMessage());
        }
    }

    private static void balanceInquiry(Scanner scanner) {
        System.out.print("Enter Account Number: ");
        long accountNumber = scanner.nextLong();
        List<String> accounts = readAccounts();

        for (String account : accounts) {
            String[] details = account.split(",");
            long accNum = Long.parseLong(details[1]);
            if (accNum == accountNumber) {
                System.out.println("Current Balance:" + details[3]);
                return;
            }
        }
        System.out.println("Account not found.");
    }

    private static void updateAccount(Scanner scanner) {
        List<String> accounts = readAccounts();
        System.out.print("Enter Account Number: ");
        long accountNumber = scanner.nextLong();
        System.out.print("Enter New Name: ");
        String newName = scanner.next();
        System.out.print("Enter New Age: ");
        int newAge = scanner.nextInt();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String account : accounts) {
                String[] details = account.split(",");
                long accNum = Long.parseLong(details[1]);
                if (accNum == accountNumber) {
                    writer.write(newName + "," + accNum + "," + newAge + "," + details[3]);
                    found = true;
                } else {
                    writer.write(account);
                }
                writer.newLine();
            }
            if (found) {
                System.out.println("Account updated successfully!");
            } else {
                System.out.println("Account not found.");
            }
        } catch (IOException e) {
            System.err.println("Error updating account: " + e.getMessage());
        }
    }

    private static void deleteAccount(Scanner scanner) {
        List<String> accounts = readAccounts();
        System.out.print("Enter Account Number: ");
        long accountNumber = scanner.nextLong();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String account : accounts) {
                String[] details = account.split(",");
                long accNum = Long.parseLong(details[1]);
                if (accNum == accountNumber) {
                    found = true;
                } else {
                    writer.write(account);
                    writer.newLine();
                }
            }
            if (found) {
                System.out.println("Account deleted successfully!");
            } else {
                System.out.println("Account not found.");
            }
        } catch (IOException e) {
            System.err.println("Error deleting account: " + e.getMessage());
        }
    }

    private static List<String> readAccounts() {
        List<String> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                accounts.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading accounts: " + e.getMessage());
        }
        return accounts;
    }
}
