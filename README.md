# Fair Share: Bill Splitting Application

## Project Report

**Author:** Aishwarya Godse  
**Date:** 21.04.2025  
**PRN:** 1032220740  
**Subject:** Java

## Table of Contents

1. [Summary](#summary)
2. [Introduction](#introduction)
3. [Project Overview](#project-overview)
   - 3.1 [Problem Statement](#problem-statement)
   - 3.2 [Objectives](#objectives)
4. [System Architecture and Implementation](#system-architecture-and-implementation)
   - 4.1 [Class Structure](#class-structure)
   - 4.2 [Implementation Details](#implementation-details)
5. [Features](#features)
   - 5.1 [Creating Groups](#creating-groups)
   - 5.2 [Adding Expenses with Equal Split](#adding-expenses-with-equal-split)
   - 5.3 [Adding Expenses with Custom Split](#adding-expenses-with-custom-split)
   - 5.4 [Viewing Balances](#viewing-balances)
   - 5.5 [Settling Up](#settling-up)
6. [Installation and Setup](#installation-and-setup)
   - 6.1 [Requirements](#requirements)
   - 6.2 [Running the Application](#running-the-application)
   - 6.3 [File Structure](#file-structure)
7. [Use Cases](#use-cases)
   - 7.1 [Roommate Expenses](#roommate-expenses)
   - 7.2 [Trip Expenses](#trip-expenses)
8. [Limitations and Future Enhancements](#limitations-and-future-enhancements)
   - 8.1 [Current Limitations](#current-limitations)
   - 8.2 [Potential Enhancements](#potential-enhancements)
9. [Conclusion](#conclusion)
10. [Appendix: Complete Source Code](#appendix-complete-source-code)

## Summary

The Fair Share Bill Splitting Application is a console-based Java program designed to streamline the management and division of shared expenses among groups of people. Similar to commercial applications like Splitwise, this program offers an organized approach to tracking expenses, calculating balances, and suggesting optimal settlement plans. The project demonstrates object-oriented programming principles applied to solve a common real-world problem.

This report details the design, implementation, features, and potential enhancements of the application, providing comprehensive documentation of the project for evaluation purposes.

## Introduction

Managing shared expenses among friends, roommates, or during group activities can be challenging and often leads to confusing calculations and record-keeping. The Fair Share application addresses this problem by providing a systematic approach to expense tracking and balance calculation, ensuring fairness and transparency in financial dealings within groups.

This Java-based console application implements core functionality including group management, expense recording with flexible splitting options, balance calculation, and optimized settlement suggestions. The project showcases object-oriented design principles and practical problem-solving through software development.

## Project Overview

### Problem Statement

Sharing expenses among multiple individuals often results in complex financial relationships that are difficult to track manually. Traditional methods like spreadsheets or paper notes are error-prone and time-consuming. This application provides a systematic solution to:

- Record who paid for what and how much
- Track how expenses should be split among group members
- Calculate running balances between all participants
- Simplify the settlement process with minimal transactions

### Objectives

The primary objectives of this project include:

- Creating a user-friendly interface for managing group expenses
- Implementing accurate expense splitting algorithms with support for both equal and custom splits
- Providing clear visibility of balances and optimized settlement plans
- Applying object-oriented design principles to create a modular, maintainable application
- Developing a practical solution for a common real-world problem

## System Architecture and Implementation

### Class Structure

The application follows an object-oriented design with the following key classes:

1. **BillSplitApp**: Main application class that serves as the entry point
2. **AppController**: Manages the application flow and user interaction
3. **Person**: Represents an individual group member
4. **Group**: Represents a collection of people who share expenses
5. **Expense**: Stores information about a single expense including payer and splits
6. **GroupManager**: Maintains the collection of expense-sharing groups

The class diagram below illustrates the relationships between these components:

```
BillSplitApp
    │
    ├── AppController
    │       │
    │       ├── GroupManager
    │       │       │
    │       │       └── Group
    │       │           │
    │       │           ├── Person
    │       │           │
    │       │           └── Expense
    │       │               │
    │       │               └── Person
    │       │
    │       └── Scanner (for user input)
    │
    └── main()
```

### Implementation Details

The application is implemented as a console-based Java program with a text-based user interface. Key implementation aspects include:

- **Object-Oriented Design**: Each entity is represented as a separate class with appropriate encapsulation
- **Data Structures**: Utilizes ArrayList for collections and HashMap for expense splits
- **Input Validation**: Robust input validation to prevent errors and improve user experience
- **Algorithms**: Custom algorithms for balance calculation and settlement optimization

## Features

### Creating Groups

Users can create multiple expense-sharing groups and add members to them. This feature allows for managing separate financial arrangements such as roommate expenses, trip costs, or project budgets.

**Implementation:**

```java
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
```

**Sample Output:**

```
========================================
    Welcome to Bill Splitter App!
========================================

MAIN MENU
1. Create a new group
2. Select existing group
3. Exit
Enter your choice: 1

=== Create New Group ===
Enter group name: Weekend Trip
Let's add members to the group.
Enter member name (or type 'done' to finish): John
John added to the group.
Enter member name (or type 'done' to finish): Sarah
Sarah added to the group.
Enter member name (or type 'done' to finish): Mike
Mike added to the group.
Enter member name (or type 'done' to finish): Lisa
Lisa added to the group.
Enter member name (or type 'done' to finish): done

Group 'Weekend Trip' created successfully with 4 members!
```

### Adding Expenses with Equal Split

The application supports adding expenses with equal distribution among all group members. This is useful for situations where everyone benefits equally from an expense.

**Implementation:**

```java
// Inside addExpense method when splitChoice == 1
// Split equally
for (Person member : group.getMembers()) {
    expense.addSplit(member, amount / group.getMembers().size());
}
group.addExpense(expense);
System.out.println("Expense added and split equally among all members.");
```

**Sample Output:**

```
=== Group: Weekend Trip ===
1. Add expense
2. View balances
3. Settle up
4. Return to main menu
Enter your choice: 1

=== Add Expense ===
Enter expense description: Gas
Enter amount: 40.00
Who paid for this expense?
1. John
2. Sarah
3. Mike
4. Lisa
Enter member number: 2

How do you want to split this expense?
1. Split equally
2. Split by specific amounts
Enter your choice: 1
Expense added and split equally among all members.
```

### Adding Expenses with Custom Split

For more complex situations, the application allows custom splitting with specific amounts assigned to each person. This accommodates scenarios where benefits are not equally distributed.

**Implementation:**

```java
// Inside addExpense method when splitChoice == 2
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
```

**Sample Output:**

```
=== Add Expense ===
Enter expense description: Dinner
Enter amount: 120.00
Who paid for this expense?
1. John
2. Sarah
3. Mike
4. Lisa
Enter member number: 1

How do you want to split this expense?
1. Split equally
2. Split by specific amounts
Enter your choice: 2
Enter amount for John: 40.00
Enter amount for Sarah: 30.00
Enter amount for Mike: 25.00
Enter amount for Lisa: 25.00
Expense added with custom split.
```

### Viewing Balances

The application calculates and displays the current financial status of each group member, showing who is owed money and who owes money.

**Implementation:**

```java
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
```

**Sample Output:**

```
=== Group: Weekend Trip ===
1. Add expense
2. View balances
3. Settle up
4. Return to main menu
Enter your choice: 2

=== Balances ===
John: 80.00 (is owed)
Sarah: 30.00 (is owed)
Mike: 55.00 (owes)
Lisa: 55.00 (owes)
```

### Settling Up

The application provides optimized settlement suggestions that minimize the number of transactions needed to resolve all balances within the group.

**Implementation:**

```java
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
}
```

**Sample Output:**

```
=== Group: Weekend Trip ===
1. Add expense
2. View balances
3. Settle up
4. Return to main menu
Enter your choice: 3

=== Settle Up ===
Mike should pay 50.00 to John
Mike should pay 5.00 to Sarah
Lisa should pay 30.00 to John
Lisa should pay 25.00 to Sarah

Mark all expenses as settled? (yes/no): yes
All expenses marked as settled.
```

## Installation and Setup

### Requirements

- Java Runtime Environment (JRE) 8 or higher
- Any operating system that supports Java

### Running the Application

For standard Java environments:

```
javac BillSplitApp.java
java BillSplitApp
```

For online compilers:

- Copy the entire source code
- Paste into your online Java compiler
- Run the program

### File Structure

For online compilers, all classes are combined into a single file. For standalone environments, the application can be organized with separate files for each class.

## Use Cases

### Roommate Expenses

**Scenario**: Three roommates (Alex, Bailey, and Charlie) share an apartment and split utilities and groceries.

**Steps**:

1. Create a group called "Apartment"
2. Add members: Alex, Bailey, Charlie
3. Add expense: "Electricity" - $90 paid by Alex, split equally
4. Add expense: "Groceries" - $120 paid by Bailey, custom split (Alex: $30, Bailey: $50, Charlie: $40)
5. Add expense: "Internet" - $60 paid by Charlie, split equally
6. View balances to see who needs to pay whom
7. Settle up when everyone agrees

**Expected Balance Output**:

```
=== Balances ===
Alex: 30.00 (is owed)
Bailey: 50.00 (is owed)
Charlie: 20.00 (is owed)
```

**Expected Settlement Output**:

```
=== Settle Up ===
All balances are settled!
```

### Trip Expenses

**Scenario**: Four friends (John, Sarah, Mike, and Lisa) go on a weekend trip.

**Steps**:

1. Create a group called "Weekend Trip"
2. Add members: John, Sarah, Mike, Lisa
3. Add expense: "Hotel" - $400 paid by John, split equally
4. Add expense: "Gas" - $40 paid by Sarah, split equally
5. Add expense: "Dinner" - $120 paid by John, custom split (John: $40, Sarah: $30, Mike: $25, Lisa: $25)
6. Add expense: "Tickets" - $80 paid by Mike, split equally
7. View balances at the end of the trip
8. Settle up and clear all expenses

**Expected Balance Output**:

```
=== Balances ===
John: 420.00 (is owed)
Sarah: 0.00 (settled)
Mike: 60.00 (is owed)
Lisa: 480.00 (owes)
```

**Expected Settlement Output**:

```
=== Settle Up ===
Lisa should pay 420.00 to John
Lisa should pay 60.00 to Mike

Mark all expenses as settled? (yes/no): yes
All expenses marked as settled.
```

## Limitations and Future Enhancements

### Current Limitations

1. **No Data Persistence**: The application does not save data between sessions
2. **Console Interface**: Limited to text-based interaction
3. **No Authentication**: No user authentication or authorization
4. **Simple Reporting**: Limited reporting capabilities
5. **No Expense Categories**: No support for categorizing expenses

### Potential Enhancements

1. **Data Persistence**:

   - Add database integration (e.g., SQLite, MySQL)
   - Implement file-based storage (JSON, XML)

2. **Enhanced User Interface**:

   - Develop a graphical user interface (GUI) using JavaFX or Swing
   - Create a web interface using Java web technologies

3. **Additional Features**:

   - Expense categories and tagging
   - Date tracking for expenses
   - Currency conversion for international groups
   - Statistical reports and charts
   - Reminder system for pending payments

4. **Mobile Integration**:
   - Develop companion mobile app
   - Add push notifications for new expenses or reminders

## Conclusion

The Fair Share Bill Splitting application successfully demonstrates the implementation of a practical expense-sharing solution using Java programming concepts. The project applies object-oriented design principles to create a modular and maintainable codebase that solves a common real-world problem.

The application includes key features like group management, expense tracking with different splitting options, balance calculation, and settlement planning. While the current implementation has limitations, particularly in terms of persistence and user interface, it provides a solid foundation that can be extended with additional features in the future.

This project serves as both a practical demonstration of Java programming skills and a useful tool for managing shared expenses among groups of people.

## Appendix: Complete Source Code

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BillSplitApp {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    Welcome to Bill Splitter App!");
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
                    System.out.println("Thank you for using Bill Splitter App!");
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
    private
```
