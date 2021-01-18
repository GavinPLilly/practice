class ArrayDups {
    public static int removeDuplicates(int[] nums) {
        int l = nums.length;
        int write_index = 0;
        int dups = 0;

        int i = 1;
        while(i < l) {
            int j = i;
            while(nums[write_index] == nums[j]) {
                dups++;
                j++;
                if(j >= l) {
                    return l - dups;
                }
            }
            nums[++write_index] = nums[j];
            i = j + 1;
        }
        return l - dups;
    }
    public static void main(String[] agrs) {
        int[] arr = {1, 1, 1};
        System.out.println(ArrayDups.removeDuplicates(arr));
        for(int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            System.out.print(", ");
        }
        System.out.println("");
    }
}
