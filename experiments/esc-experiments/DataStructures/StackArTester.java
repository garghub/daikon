package DataStructures;

import java.util.*;

public class StackArTester
{
  private StackAr s = new StackAr(0);

  private StackArTester() { }

  public void doNew(int size)
  {
    s = new StackAr(size);
    observe();
  }

  private int phase;
  private final int maxPhase = 5;
  public Object createItem(int i)
  {
    switch (phase) {
    case 0:
      return new MyInteger(i);
    case 1:
      return new Integer(i);
    case 2:
      return new Double(i);
    case 3:
      return new Float(i);
    case 4:
      return new int[i];
    default:
      return new int[i][];
    }
  }

  public void push(int i)
  {
    try {
      s.push(createItem(i));
      observe();
    } catch (Overflow e) { }
  }

  public void topAndPop()
  {
    s.topAndPop();
    observe();
  }
  public void top()
  {
    s.top();
    observe();
  }

  public void observe()
  {
    s.isFull();
    s.isEmpty();
    s.top();
  }

  public void repPush(int n)
  {
    for (int i=0; i < n; i++) {
      int x = (int)(1000 * Math.random());
      push(x);
    }
  }

  public void popAll()
  {
    while( !s.isEmpty( ) ) {
      topAndPop();
    }
    top();
  }

  public void makeEmpty() {
    observe();
    s.makeEmpty();
    observe();
  }

  public void fillAndEmpty(int n)
  {
    doNew(n);
    repPush(n);
    popAll();
    doNew(n);
    repPush(n);
    makeEmpty();
  }

  public void fillWithSame(int n)
  {
    doNew(n);
    Object elt = createItem(n);
    for (int i=0; i < n; i++) {
      try {
	s.push(elt);
        observe();
      } catch (Overflow e) { }
    }
    // popAll();
  }

  public void run_long()
  {
    for (phase = 0; phase <= maxPhase; phase++) {
      fillAndEmpty(0);
      fillAndEmpty(1);
      fillAndEmpty(2);
      fillAndEmpty(5);
      fillAndEmpty(10);
      fillAndEmpty(20);
      fillAndEmpty(50);
      fillAndEmpty(100);
      fillWithSame(7);
      fillWithSame(22);
    }
  }

  public void run_short()
  {
    phase = 0; fillAndEmpty(0);
    phase = 1; fillAndEmpty(1);
    phase = 2; fillAndEmpty(2);
    phase = 3; fillAndEmpty(5);
    phase = 4; fillAndEmpty(10);
    phase = 0; fillAndEmpty(20);
    phase = 1; fillAndEmpty(50);
    phase = 2; fillAndEmpty(100);
    phase = 3; fillWithSame(7);
    phase = 4; fillWithSame(22);
  }

  public void run() {
    run_long();
  }

  public static void main(String[] args)
  {
    (new StackArTester()).run();
  }
}
