#include <iostream>
#include <climits>
#include <cmath>
#include <algorithm>
#include <chrono>

using namespace std;
using namespace chrono;

int partition(int A[], int low, int high) {
    int pivot = A[high];
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (A[j] <= pivot) {
            i++;
            swap(A[i], A[j]);
        }
    }

    swap(A[i + 1], A[high]);
    return i + 1;
}

void quickSort(int A[], int low, int high) {
    if (low < high) {
        int pivotIndex = partition(A, low, high);
        quickSort(A, low, pivotIndex - 1);
        quickSort(A, pivotIndex + 1, high);
    }
}

int MinDistanceOptimized(int A[], int length) {
    if (length < 2) {
        return INT_MAX;
    }

    quickSort(A, 0, length - 1);
    int dmin = INT_MAX;

    for (int i = 1; i < length; i++) {
        int current_distance = abs(A[i] - A[i - 1]);
        if (current_distance < dmin) {
            dmin = current_distance;
        }
    }

    return dmin;
}

int main() {
    int size;
    auto start = high_resolution_clock::now();
    cout << "Enter the size of the array: ";
    cin >> size;

    if (size < 2) {
        cout << "Array size should be at least 2." << endl;
        return 1;
    }

    int* array = new int[size];

    cout << "Enter " << size << " numbers: ";
    for (int i = 0; i < size; i++) {
        cin >> array[i];
    }

    
    int minDist = MinDistanceOptimized(array, size);
    auto end = high_resolution_clock::now();
    auto duration = duration_cast<milliseconds>(end - start); 
    cout << "Minimum distance between two elements: " << minDist << endl;
    cout << "Execution time: " << duration.count() << " milliseconds" << endl;

    delete[] array;
    return 0;
}