🏦 Bank-Management-System
# Bank Management System 💳

A simple console-based Java application that simulates core banking functionalities such as account creation, security PIN encryption, and account lookup using JDBC and MySQL.

## 🚀 Features

- Create a new bank account using email
- Automatically generate a unique account number
- Securely encrypt and store security PIN
- Retrieve account details using email
- Simple character-based encryption and decryption
- Persistent storage using MySQL database

## 🛠️ Technologies Used

- Java (JDK 8+)
- JDBC (Java Database Connectivity)
- MySQL
- SQL (DDL & DML)
- Command-line input/output

## 📁 Project Structure

src/
│
├── Accounts.java # Account operations: open, check, generate account number
├── DBConnection.java # Handles MySQL database connection
├── Main.java # Entry point for user interaction
└── SecurityUtils.java # Handles encryption & decryption of security PIN
