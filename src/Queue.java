public class Queue<E>{

    // head--tail  <---- add here
    private E[] queue;
    private static int CAPACITY = Constant.maxConcurrent;
    private int tail = -1;
    private int head = -1;
    private boolean empty = true;
    @SuppressWarnings("unchecked")
    /* I DON'T GIVE A FUCK ABOUT WARNINGS*/
    public Queue(int optionalSize){
        CAPACITY = optionalSize;
        queue = (E[]) new Object[CAPACITY];
    }
    public Queue(){
        queue = (E[]) new Object[CAPACITY];
    }
    public boolean add(E e){
        if(empty)
        {
            head = tail = 0;
            queue[head] = e;
            empty = false;
            return true;
        }
        int temp = tail + 1;
        if(temp == CAPACITY)
            temp = 0;
        if(temp == head) return false;
        tail = temp;
        queue[tail] = e;
        return true;
    }
    private void deleteHead(){
        if(isEmpty()) return;
        if(head == tail) // 只有一个元素
        {
            head = tail = -1;
            empty = true;
            return;
        }
        ++head;
        if(head == CAPACITY)
            head = 0;
    }
    private void deleteTail(){
        if(isEmpty()) return;
        if(head == tail) // 只有一个元素
        {
            head = tail = -1;
            empty = true;
            return;
        }
        --tail;
        if(tail == -1)
            tail = CAPACITY-1;
    }
    public E getHead(){
        if(isEmpty()) return null;
        return queue[head];
    }
    public E getTail(){
        if(isEmpty()) return null;
        return queue[tail];
    }
    public E popHead(){
        if(isEmpty()) return null;
        E e = queue[head];
        deleteHead();
        return e;
    }
    public E popTail(){
        if(isEmpty()) return null;
        E e = queue[tail];
        deleteTail();
        return e;
    }
    public boolean isEmpty(){
        return queue == null || empty;
    }
    public int getSize(){
        if(isEmpty()) return 0;
        if(tail>head) return tail-head+1;
        if(tail<head) return CAPACITY - (head-tail-1);
        return 1;
    }
    public void clear(){
        head = -1;
        tail = -1;
        empty = true;
    }
}
