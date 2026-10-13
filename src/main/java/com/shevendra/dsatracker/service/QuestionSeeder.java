package com.shevendra.dsatracker.service;

import com.shevendra.dsatracker.model.Question;
import com.shevendra.dsatracker.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class QuestionSeeder implements CommandLineRunner {
    private final QuestionRepository repository;
    public QuestionSeeder(QuestionRepository repository) { this.repository = repository; }

    @Override public void run(String... args) {
        Map<String, List<String>> data = new LinkedHashMap<>();
        data.put("Arrays", List.of(
            "Largest Element in Array", "Second Largest Element", "Check if Array Is Sorted", "Reverse an Array", "Remove Duplicates from Sorted Array", "Left Rotate Array by One", "Left Rotate Array by K Places", "Move Zeroes to End", "Linear Search", "Missing Number", "Maximum Consecutive Ones", "Single Number", "Leaders in an Array", "Longest Subarray with Given Sum", "Two Sum", "Sort 0s, 1s and 2s", "Find Union of Two Sorted Arrays", "Find Intersection of Two Arrays", "Majority Element", "Best Time to Buy and Sell Stock", "Rearrange Array by Sign", "Next Permutation", "Maximum Subarray Sum (Kadane's Algorithm)", "Longest Consecutive Sequence", "Set Matrix Zeroes", "Rotate Matrix by 90 Degrees", "Spiral Matrix", "Subarray Sum Equals K", "Product of Array Except Self", "Merge Overlapping Intervals", "3Sum", "4Sum", "Trapping Rain Water", "Container With Most Water", "Find Duplicate Number", "Find Missing and Repeating Number", "Count Inversions", "Maximum Product Subarray"));
        data.put("Strings", List.of("Reverse a String", "Check Palindrome", "Check Anagram", "Remove Duplicate Characters", "Count Character Frequency", "First Non-Repeating Character", "First Repeating Character", "Valid Anagram", "Reverse Words in a String", "Longest Common Prefix", "Valid Palindrome II", "String Compression", "Longest Palindromic Substring", "Longest Substring Without Repeating Characters", "Group Anagrams", "String to Integer (atoi)", "Roman to Integer", "Find Substring (strStr)", "Minimum Window Substring"));
        data.put("Hashing", List.of("Frequency of Elements", "Two Sum Using HashMap", "Contains Duplicate", "Intersection of Two Arrays", "Union of Two Arrays", "Majority Element", "Longest Consecutive Sequence", "Subarray Sum Equals K", "Count Subarrays with Given XOR", "Longest Subarray with Sum Zero", "Isomorphic Strings", "Word Pattern", "Group Anagrams", "Happy Number", "4Sum II"));
        data.put("Binary Search", List.of("Binary Search", "Search Insert Position", "First and Last Position of Element", "Count Occurrences in Sorted Array", "Search in Rotated Sorted Array", "Find Minimum in Rotated Sorted Array", "Single Element in Sorted Array", "Peak Element", "Square Root of a Number", "Find Floor and Ceil", "Koko Eating Bananas", "Minimum Days to Make Bouquets", "Capacity to Ship Packages Within D Days", "Aggressive Cows", "Allocate Minimum Pages", "Median of Two Sorted Arrays"));
        data.put("Two Pointers", List.of("Two Sum in Sorted Array", "Remove Duplicates from Sorted Array", "Move Zeroes", "Reverse Vowels of a String", "Valid Palindrome", "Squares of a Sorted Array", "Container With Most Water", "3Sum", "3Sum Closest", "Sort Colors", "Merge Two Sorted Arrays", "Trapping Rain Water", "Pair with Given Difference", "Backspace String Compare"));
        data.put("Sliding Window", List.of("Maximum Sum Subarray of Size K", "First Negative Integer in Every Window", "Count Occurrences of Anagrams", "Longest Substring Without Repeating Characters", "Max Consecutive Ones III", "Fruit Into Baskets", "Longest Repeating Character Replacement", "Permutation in String", "Minimum Window Substring", "Minimum Size Subarray Sum", "Sliding Window Maximum", "Subarrays with K Different Integers", "Binary Subarrays With Sum", "Longest Subarray with At Most K Zeros"));
        data.put("Linked List", List.of("Implement Singly Linked List", "Traverse and Search in Linked List", "Insert at Beginning and End", "Delete a Node", "Reverse Linked List", "Middle of Linked List", "Detect Cycle in Linked List", "Find Starting Point of Cycle", "Remove Nth Node from End", "Merge Two Sorted Lists", "Palindrome Linked List", "Intersection of Two Linked Lists", "Add Two Numbers", "Reverse Nodes in K-Group", "Sort a Linked List", "Flatten a Linked List", "Copy List with Random Pointer"));
        data.put("Stack and Queue", List.of("Implement Stack Using Array", "Implement Queue Using Array", "Implement Stack Using Queue", "Implement Queue Using Stacks", "Valid Parentheses", "Min Stack", "Next Greater Element", "Next Smaller Element", "Daily Temperatures", "Stock Span Problem", "Largest Rectangle in Histogram", "Maximal Rectangle", "Evaluate Reverse Polish Notation", "Sliding Window Maximum", "Circular Tour / Gas Station", "Rotten Oranges using Queue", "LRU Cache"));
        data.forEach((topic, titles) -> titles.forEach(title -> {
            if (!repository.existsByTopicAndTitle(topic, title)) repository.save(new Question(topic, title));
        }));
    }
}
