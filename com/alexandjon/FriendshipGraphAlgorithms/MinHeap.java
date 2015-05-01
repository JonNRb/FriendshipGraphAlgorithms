package com.alexandjon.FriendshipGraphAlgorithms;

import java.lang.*;
import java.util.*;
import java.io.*;


public class MinHeap<S,T extends Comparable<T>>{
	public static class HeapNode<S,T extends Comparable<T>> {
		S data;
		T weight;

		public HeapNode(S data, T weight) {
			this.data = data;
			this.weight = weight;
		}

		public String toString() {
			return "(" + data.toString() + ", " + weight.toString() + ")";
		}
	}

	private ArrayList<HeapNode<S,T>> heap;

	public MinHeap() {
		heap = new ArrayList<>();
	}

	public void insertNode(S data, T weight) {
		HeapNode<S,T> newNode = new HeapNode<>(data, weight);
		heap.add(newNode);

		int pre = heap.size() - 1, cur = (pre - 1)/2;
		HeapNode<S,T> current = heap.get(cur);
		while (cur >= 0 && current.weight.compareTo(newNode.weight) > 0) {
			heap.set(pre, current);
			heap.set(cur, newNode);

			pre = cur;
			cur = (cur - 1)/2;
			current = heap.get(cur);
		}
	}

	public boolean empty() { return heap.size() == 0; }

	public HeapNode<S,T> pop() {
		if (empty()) return null;

		HeapNode<S,T> retVal = heap.get(0);
		
		HeapNode<S,T> last = heap.get(heap.size()-1);
		heap.remove(heap.size()-1);
		if (empty()) {
			return retVal;
		}
		heap.set(0, last);

		int pre = 0, cur = pre*2 + 1;
		while ((cur < heap.size() && last.weight.compareTo(heap.get(cur).weight) > 0)
				|| (cur + 1 < heap.size() && last.weight.compareTo(heap.get(cur+1).weight) > 0)) {
			
			int minChild;
			if (cur + 1 >= heap.size()) minChild = cur;
			else minChild = (heap.get(cur).weight.compareTo(heap.get(cur+1).weight) < 0 ? cur : cur+1);

			heap.set(pre, heap.get(minChild));
			heap.set(minChild, last);

			pre = minChild;
			cur = pre*2 + 1;
		}


		return retVal;
	}

	public void printHeap() {
		int n = 1;
		for (int i = 0; i < heap.size(); n *= 2) {
			for (int j = 0; j < n && i + j < heap.size(); j++) {
				System.out.print(heap.get(i+j).toString());
				if (j+1 < n && i+j+1 < heap.size()) System.out.print(" , ");
			}
			i += n;
			System.out.println();
		}
	}
}
