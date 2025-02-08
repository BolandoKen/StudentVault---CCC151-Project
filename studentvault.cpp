#include <iostream>
#include <vector>
#include <climits>
#include <cmath>
#include <algorithm>
#include <chrono>
#include <stdexcept>

using namespace std;
using namespace chrono;

class MinDistanceFinder {
private:
    static void heapify(vector<int>& arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest])
            largest = left;

        if (right < n && arr[right] > arr[largest])
            largest = right;

        if (largest != i) {
            swap(arr[i], arr[largest]);
            heapify(arr, n, largest);
        }
    }

    static void heapSort(vector<int>& arr) {
        int n = arr.size();

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            swap(arr[0], arr[i]);
            heapify(arr, i, 0);
        }
    }

public:
    static pair<int, pair<int, int>> findMinDistance(const vector<int>& input) {
        if (input.size() < 2) {
            throw invalid_argument("Array must contain at least 2 elements");
        }

        vector<int> A = input; // Create a copy to avoid modifying original
        heapSort(A);
        
        int dmin = INT_MAX;
        int num1 = 0, num2 = 0;

        for (size_t i = 1; i < A.size(); i++) {
            int current_distance = abs(A[i] - A[i - 1]);
            if (current_distance < dmin) {
                dmin = current_distance;
                num1 = A[i - 1];
                num2 = A[i];
            }
        }

        return {dmin, {num1, num2}};
    }
};

int main() {
    try {
        auto start = high_resolution_clock::now();
        
        size_t size;
        cout << "Enter the size of the array: ";
        cin >> size;

        vector<int> numbers;
        numbers.reserve(size);

        cout << "Enter " << size << " numbers: ";
        for (size_t i = 0; i < size; i++) {
            int num;
            cin >> num;
            numbers.push_back(num);
        }

        auto [minDist, pair] = MinDistanceFinder::findMinDistance(numbers);
        
        auto end = high_resolution_clock::now();
        auto duration = duration_cast<microseconds>(end - start);

        cout << "\nResults:" << endl;
        cout << "Minimum distance: " << minDist << endl;
        cout << "Between numbers: " << pair.first << " and " << pair.second << endl;
        cout << "Execution time: " << duration.count() << " microseconds" << endl;

    } catch (const exception& e) {
        cerr << "Error: " << e.what() << endl;
        return 1;
    }

    return 0;
}