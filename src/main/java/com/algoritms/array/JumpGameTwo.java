package com.algoritms.array;

/**
 * You are given a 0-indexed array of integers nums of length n. You are initially positioned at nums[0].
 * <p>
 * Each element nums[i] represents the maximum length of a forward jump from index i.
 * In other words, if you are at nums[i], you can jump to any nums[i + j] where:
 * <p>
 * 0 <= j <= nums[i] and
 * i + j < n
 * Return the minimum number of jumps to reach nums[n - 1]. The test cases are generated such that you can reach nums[n - 1].
 * <p>
 * Input: nums = [2,3,1,1,4]
 * Output: 2
 * Explanation: The minimum number of jumps to reach the last index is 2. Jump 1 step from index 0 to 1, then 3 steps to the last index.
 */
public class JumpGameTwo {
    public static void main(String[] args) {
        // Greedy algorithm version
        //минимальное кол-во шагов чтобы добраться до n-1
        var input = new int[]{2, 3, 1, 1, 4};
        System.out.println(jump(input));
    }

    private static int jump(int[] input) {
        var result = 0;
        var currentState = 0;
        var longestJump = 0;
        for (int currentStep = 0; currentStep < input.length - 1; currentStep++) {
            longestJump = Math.max(longestJump, currentStep + input[currentStep]);
            if (currentStep == currentState) { // делаем прыжок и записываем текущее состояние, доходим до индекса где мы сейчас находимся
                currentState = longestJump; // дописываем максимальное дальнее растояние до которого удалось добраться, текущее или предыдущее
                result++;
            }
        }
        return result;
    }
}
