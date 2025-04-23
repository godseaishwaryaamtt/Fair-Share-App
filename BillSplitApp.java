import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BillSplitApp {
public static void main(String[] args) {
System.out.println("========================================");
System.out.println(" Welcome to Fair Share App!");
System.out.println("========================================");

        AppController controller = new AppController();
        controller.start();
    }

}

class AppController {
private GroupManager groupManager;
private Scanner scanner;

    public AppController() {
        this.groupManager = new GroupManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createNewGroup();
                    break;
                case 2:
                    selectGroup();
                    break;
                case 3:
                    running = false;
                    System.out.println("Thank you for using Fair Share App!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("1. Create a new group");
        System.out.println("2. Select existing group");
        System.out.println("3. Exit");
    }

    private void createNewGroup() {
        System.out.println("\n=== Create New Group ===");
        String groupName = getStringInput("Enter group name: ");

        Group group = new Group(groupName);

        boolean addingMembers = true;
        System.out.println("Let's add members to the group.");

        while (addingMembers) {
            String memberName = getStringInput("Enter member name (or type 'done' to finish): ");

            if (memberName.equalsIgnoreCase("done")) {
                addingMembers = false;
            } else {
                Person person = new Person(memberName);
                group.addMember(person);
                System.out.println(memberName + " added to the group.");
            }
        }

        groupManager.addGroup(group);
        System.out.println("\nGroup '" + groupName + "' created successfully with "
                          + group.getMembers().size() + " members!");
    }

    private void selectGroup() {
        if (groupManager.getGroups().isEmpty()) {
            System.out.println("No groups exist yet. Please create a group first.");
            return;
        }

        System.out.println("\n=== Select Group ===");
        groupManager.listGroups();

        int groupIndex = getIntInput("Enter group number: ") - 1;

        if (groupIndex >= 0 && groupIndex < groupManager.getGroups().size()) {
            Group selectedGroup = groupManager.getGroups().get(groupIndex);
            manageGroup(selectedGroup);
        } else {
            System.out.println("Invalid group number.");
        }
    }

    private void manageGroup(Group group) {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== Group: " + group.getName() + " ===");
            System.out.println("1. Add expense");
            System.out.println("2. View balances");
            System.out.println("3. Settle up");
            System.out.println("4. Return to main menu");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addExpense(group);
                    break;
                case 2:
                    viewBalances(group);
                    break;
                case 3:
                    settleUp(group);
                    break;
                case 4:
                    managing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addExpense(Group group) {
        System.out.println("\n=== Add Expense ===");
        String description = getStringInput("Enter expense description: ");
        double amount = getDoubleInput("Enter amount: ");

        // Select who paid
        System.out.println("Who paid for this expense?");
        group.listMembers();
        int payerIndex = getIntInput("Enter member number: ") - 1;

        if (payerIndex < 0 || payerIndex >= group.getMembers().size()) {
            System.out.println("Invalid member number.");
            return;
        }

        Person payer = group.getMembers().get(payerIndex);

        // Select splitting method
        System.out.println("\nHow do you want to split this expense?");
        System.out.println("1. Split equally");
        System.out.println("2. Split by specific amounts");

        int splitChoice = getIntInput("Enter your choice: ");

        Expense expense = new Expense(description, amount, payer);

        if (splitChoice == 1) {
            // Split equally
            for (Person member : group.getMembers()) {
                expense.addSplit(member, amount / group.getMembers().size());
            }
            group.addExpense(expense);
            System.out.println("Expense added and split equally among all members.");

        } else if (splitChoice == 2) {
            // Split by specific amounts
            double totalSpecified = 0;

            for (Person member : group.getMembers()) {
                double memberAmount = getDoubleInput("Enter amount for " + member.getName() + ": ");
                expense.addSplit(member, memberAmount);
                totalSpecified += memberAmount;
            }

            if (Math.abs(totalSpecified - amount) > 0.01) {
                System.out.println("Warning: The sum of individual amounts (" + totalSpecified
                                  + ") doesn't match the total expense (" + amount + ").");
                System.out.println("Please try again.");
                return;
            }

            group.addExpense(expense);
            System.out.println("Expense added with custom split.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void viewBalances(Group group) {
        System.out.println("\n=== Balances ===");
        if (group.getExpenses().isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        // Calculate and display balances
        for (Person person : group.getMembers()) {
            double balance = 0;

            // Calculate what this person paid
            for (Expense expense : group.getExpenses()) {
                if (expense.getPayer().equals(person)) {
                    balance += expense.getAmount();
                }
            }

            // Calculate what this person owes
            for (Expense expense : group.getExpenses()) {
                Double split = expense.getSplits().get(person);
                if (split != null) {
                    balance -= split;
                }
            }

            System.out.printf("%s: %.2f (%s)\n",
                person.getName(),
                Math.abs(balance),
                balance > 0 ? "is owed" : balance < 0 ? "owes" : "settled"
            );
        }
    }

    private void settleUp(Group group) {
        System.out.println("\n=== Settle Up ===");
        // Display settlement suggestions
        double[][] balances = new double[group.getMembers().size()][group.getMembers().size()];

        // Calculate what each person owes to each other
        for (Expense expense : group.getExpenses()) {
            Person payer = expense.getPayer();
            int payerIndex = group.getMembers().indexOf(payer);

            for (Person debtor : expense.getSplits().keySet()) {
                double amount = expense.getSplits().get(debtor);
                int debtorIndex = group.getMembers().indexOf(debtor);

                if (payerIndex != debtorIndex) {
                    balances[debtorIndex][payerIndex] += amount;
                }
            }
        }

        // Simplify and show the settlements
        boolean settlementsExist = false;

        for (int i = 0; i < group.getMembers().size(); i++) {
            for (int j = i + 1; j < group.getMembers().size(); j++) {
                double iOwesJ = balances[i][j];
                double jOwesI = balances[j][i];

                if (iOwesJ > jOwesI) {
                    double netAmount = iOwesJ - jOwesI;
                    if (netAmount > 0.01) {
                        System.out.printf("%s should pay %.2f to %s\n",
                            group.getMembers().get(i).getName(),
                            netAmount,
                            group.getMembers().get(j).getName()
                        );
                        settlementsExist = true;
                    }
                } else if (jOwesI > iOwesJ) {
                    double netAmount = jOwesI - iOwesJ;
                    if (netAmount > 0.01) {
                        System.out.printf("%s should pay %.2f to %s\n",
                            group.getMembers().get(j).getName(),
                            netAmount,
                            group.getMembers().get(i).getName()
                        );
                        settlementsExist = true;
                    }
                }
            }
        }

        if (!settlementsExist) {
            System.out.println("All balances are settled!");
        }

        // Option to mark as settled
        if (settlementsExist) {
            String confirm = getStringInput("\nMark all expenses as settled? (yes/no): ");
            if (confirm.equalsIgnoreCase("yes")) {
                group.clearExpenses();
                System.out.println("All expenses marked as settled.");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

}

class Person {
private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}

class Group {
private String name;
private List<Person> members;
private List<Expense> expenses;

    public Group(String name) {
        this.name = name;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Person> getMembers() {
        return members;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addMember(Person person) {
        members.add(person);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void clearExpenses() {
        expenses.clear();
    }

    public void listMembers() {
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i).getName());
        }
    }

}

class Expense {
private String description;
private double amount;
private Person payer;
private Map<Person, Double> splits;

    public Expense(String description, double amount, Person payer) {
        this.description = description;
        this.amount = amount;
        this.payer = payer;
        this.splits = new HashMap<>();
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public Person getPayer() {
        return payer;
    }

    public Map<Person, Double> getSplits() {
        return splits;
    }

    public void addSplit(Person person, double amount) {
        splits.put(person, amount);
    }

}

class GroupManager {
private List<Group> groups;

    public GroupManager() {
        this.groups = new ArrayList<>();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void listGroups() {
        if (groups.isEmpty()) {
            System.out.println("No groups created yet.");
            return;
        }

        for (int i = 0; i < groups.size(); i++) {
            System.out.println((i + 1) + ". " + groups.get(i).getName() +
                              " (" + groups.get(i).getMembers().size() + " members)");
        }
    }

}
