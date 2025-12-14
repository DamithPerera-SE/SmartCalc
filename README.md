# Scientific Java Swing Calculator

## Overview
This is a **full-featured Scientific Calculator** developed using **Java Swing**.  
It is capable of performing **basic arithmetic**, **scientific calculations**, **memory operations**, and **supports keyboard input**. A **calculation history panel** keeps track of previous operations for reference.  

This project was developed by **Damith Perera**, a Software Engineering undergraduate, as part of learning Java GUI development and object-oriented programming concepts.

---

## Features

### Basic Arithmetic
- Addition (`+`), Subtraction (`-`), Multiplication (`*`), Division (`/`), Modulus (`%`)  
- Decimal numbers (floating point calculations)  
- Sequential operations without pressing `=` every time  

### Memory Operations
- **M+** : Add current value to memory  
- **M-** : Subtract current value from memory  
- **MR** : Recall memory  
- **MC** : Clear memory  

### Scientific Functions
- **Square Root (`√`)**  
- **Square (`x²`)**  
- **Reciprocal (`1/x`)**  
- **Exponentiation (`x^y`)**  
- **Trigonometric Functions**: `sin`, `cos`, `tan` (radians)  
- **Logarithmic Functions**: `log` (base 10), `ln` (natural log)  
- **Percentage (`%`)**  
- **Sign Toggle (`+/-`)**  

### Other Features
- **Backspace (`←`)** to delete the last digit  
- **Calculation History Panel** to view previous calculations  
- **Keyboard Support**: Numbers, operators, Enter key for `=`  
- **Error Handling**: Handles division by zero and invalid input gracefully  

---

## Requirements

### Software
- **Java Development Kit (JDK) 8 or higher**  
- Uses only **built-in Java Swing & AWT libraries** (no external dependencies)

### Hardware
- CPU: 1 GHz or higher  
- RAM: 512 MB+  
- Display: 1024x768 resolution or higher  

### Operating System
- Windows, Linux, or macOS  

---

## Installation & Usage

1. **Clone or download the repository**.  
2. **Compile the Java file**:  
   ```bash
   javac ScientificCalculator.java
