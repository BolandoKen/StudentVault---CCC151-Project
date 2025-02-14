import csv
import os

def add_name_to_csv():
    # Get user input
    first_name = input("Enter first_name: ")
    last_name = input("Enter last_name: ")
    while True:
        try:
            age = int(input("Enter age: "))
            break
        except ValueError:
            print("Please enter a valid number for age")
    
    # Create a dictionary with the new data
    new_entry = {
        'first_name': first_name,
        'last_name': last_name,
        'age': age
    }
    
    # Check if file exists
    file_exists = os.path.exists('names.csv')
    
    # Open file in append mode if it exists, write mode if it doesn't
    mode = 'a' if file_exists else 'w'
    with open('names.csv', mode, newline='') as file:
        fieldnames = ['first_name', 'last_name', 'age']
        writer = csv.DictWriter(file, fieldnames=fieldnames)
        
        # Write header only if creating a new file
        if not file_exists:
            writer.writeheader()
        
        # Write the new entry
        writer.writerow(new_entry)
    
    print(f"\nAdded new entry to names.csv")

def read_names_from_csv():
    try:
        # Reading from CSV
        with open('names.csv', 'r') as file:
            reader = csv.DictReader(file)
            
            # Print each row
            print("\nContents of names.csv:")
            print("-" * 40)
            print("first_name, last_name, age")
            print("-" * 40)
            for row in reader:
                print(f"{row['first_name']}, {row['last_name']}, {row['age']}")
            print("-" * 40)
    
    except FileNotFoundError:
        print("Error: names.csv file not found!")

def main():
    while True:
        print("\nCSV Operations Menu:")
        print("1. Add a new entry")
        print("2. View all entries")
        print("3. Exit")
        
        choice = input("\nEnter your choice (1-3): ")
        
        if choice == '1':
            add_name_to_csv()
        elif choice == '2':
            read_names_from_csv()
        elif choice == '3':
            print("Goodbye!")
            break
        else:
            print("Invalid choice. Please try again.")

if __name__ == "__main__":
    main()