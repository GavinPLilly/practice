class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        boolean carry = false;;
        int tmpSum = 0;
        ListNode prev = null;
        ListNode cur = null;
        ListNode out = null;
        while(l1 != null && l2 != null) {
            tmpSum = l1.val + l2.val;
            if(carry) {
                tmpSum++;
            }
            if(tmpSum > 9) {
                carry = true;
                tmpSum = tmpSum % 10;
            }
            else {
                carry = false;
            }
            if(cur == null) {
                cur = new ListNode(tmpSum);
                out = cur;
            }
            else {
                cur = new ListNode(tmpSum);
                prev.next = cur;
            }
            prev = cur;
            l1 = l1.next;
            l2 = l2.next;
        }
        while(l1 != null) {
            tmpSum = l1.val;
            if(carry) {
                tmpSum++;
            }
            if(tmpSum > 9) {
                carry = true;
                tmpSum = tmpSum % 10;
            }
            else {
                carry = false;
            }
            cur = new ListNode(tmpSum);
            prev.next = cur;
            prev = cur;
            l1 = l1.next;
        }
        while(l2 != null) {
            tmpSum = l2.val;
            if(carry) {
                tmpSum++;
            }
            if(tmpSum > 9) {
                carry = true;
                tmpSum = tmpSum % 10;
            }
            else {
                carry = false;
            }
            cur = new ListNode(tmpSum);
            prev.next = cur;
            prev = cur;
            l2 = l2.next;
        }
        if(carry) {
            cur = new ListNode(1);
            prev.next = cur;
        }
        return out;
    }

    public static void main(String[] agrs) {
        // ListNode l1 = new ListNode(1, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(1, null))))))))))));
        // ListNode l2 = new ListNode(9, null);
        ListNode t1 = new ListNode(9, null);
        ListNode t2 = new ListNode(9, t1);
        ListNode t3 = new ListNode(9, t2);
        ListNode t4 = new ListNode(9, t3);
        ListNode t5 = new ListNode(9, t4);
        ListNode t6 = new ListNode(9, t5);
        ListNode t7 = new ListNode(9, t6);
        ListNode t8 = new ListNode(9, t7);
        ListNode t9 = new ListNode(9, t8);
        ListNode l1 = new ListNode(9, t9);

        ListNode l2 = new ListNode(9, null);
        Solution s = new Solution();
        ListNode out = s.addTwoNumbers(l1, l2);
        while(out != null) {
            System.out.print(out.val + ", ");
            out = out.next;
        }
        System.out.println("");
    }
}