package uz.ncipro.calendar;

import java.beans.*;
import java.text.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import uz.ncipro.calendar.plaf.metal.MetalJDateTimePickerUI;
import uz.ncipro.calendar.plaf.motif.MotifJDateTimePickerUI;
import uz.ncipro.calendar.plaf.windows.WindowsJDateTimePickerUI;

/**
 * <p>Title: JDateTimePicker</p>
 * <p>Copyright: Copyright (c) 2001-2003</p>
 * <p>Company: NCI Projects</p>
 * @author Vadim Crits
 * @version 1.1
 */

public class JDateTimePicker extends JComboBox implements PropertyChangeListener
{
  private String displayFormat;
  private SimpleDateFormat sdf;
  protected JCalendar popupEditor;

  public JDateTimePicker()
  {
    sdf = new SimpleDateFormat();
    setEditable(true);
    setDisplayFormat(sdf.toPattern());
  }

  public void updateUI()
  {
    String currentLookAndFeel = UIManager.getLookAndFeel().getID();
    if (currentLookAndFeel.equals("Metal"))
    {
      setUI(MetalJDateTimePickerUI.createUI(this));
      popupEditor = ((MetalJDateTimePickerUI)ui).getPopupEditor();
    }
    else if (currentLookAndFeel.equals("Motif"))
    {
      setUI(MotifJDateTimePickerUI.createUI(this));
      popupEditor = ((MotifJDateTimePickerUI)ui).getPopupEditor();
    }
    else
    {
      setUI(WindowsJDateTimePickerUI.createUI(this));
      popupEditor = ((WindowsJDateTimePickerUI)ui).getPopupEditor();
    }
    popupEditor.addPropertyChangeListener(this);
  }

  private void setValue(String value)
  {
    removeAllItems();
    addItem(value);
  }

  public void setLocale(Locale l)
  {
    super.setLocale(l);
    if (ui != null)
      popupEditor.setLocale(l);
  }

  /**
   * Returns the date of this component.
   * @return the value of the date property.
   * @see #setDate
   */
  public Date getDate()
  {
    return popupEditor.getDate();
  }

  /**
   * Sets the date property.
   * @param date the new date value for this component
   * @see #getDate
   */
  public void setDate(Date date)
  {
    popupEditor.setDate(date);
  }

  /**
   * Returns the displayFormat.
   * @return the value of the displayFormat property
   * @see #setDisplayFormat
   */
  public String getDisplayFormat()
  {
    return displayFormat;
  }

  /**
   * Sets the displayFormat property.
   * @param displayFormat the new date and time pattern for this date format
   * @see #getDisplayFormat
   */
  public void setDisplayFormat(String displayFormat)
  {
    String oldDisplayFormat = this.displayFormat;
    this.displayFormat = displayFormat;
    sdf.applyPattern(displayFormat);
    setValue(sdf.format(popupEditor.getDate()));
    firePropertyChange("displayFormat", oldDisplayFormat, displayFormat);
  }

  public void actionPerformed(ActionEvent e)
  {
    super.actionPerformed(e);
    try
    {
      popupEditor.setDate(sdf.parse((String)getSelectedItem()));
    }
    catch (Exception ex)
    {
      System.out.println(ex.getMessage());
    }
  }

  public void propertyChange(PropertyChangeEvent e)
  {
    String propertyName = e.getPropertyName();
    if (propertyName.equals("date") || propertyName.equals("day") ||
      propertyName.equals("month") || propertyName.equals("year"))
      setValue(sdf.format(popupEditor.getDate()));
  }

  public static void main(String[] args)
  {
    JFrame frame = new JFrame("DateTimePicker");
    JDateTimePicker dtp = new JDateTimePicker();
    dtp.setDisplayFormat("dd.MM.yyyy");
    frame.getContentPane().add(dtp);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.show();
  }
}