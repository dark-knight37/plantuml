package ext.plantuml.com.ctreber.aclib.sort;

import java.util.Comparator;

/**
 * <p>Teehee - found that Comparator allready exists.
 *
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
abstract public class CTSort
{
  // :: remove folder when WASM
  public void sort(Object[] items)
  {
    sort(items, new DefaultComparator());
  }

  abstract public void sort(Object[] items, Comparator comparator);
}
