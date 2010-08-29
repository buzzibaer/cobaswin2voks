package ctvdkip.business;

import ctvdkip.database.voks.VoksRecord;
import ctvdkip.util.ApplicationLogger;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: rbust
 * Date: 23.08.2004
 * Time: 23:27:35
 * To change this template use File | Settings | File Templates.
 */
public class CobasWin {

    public CobasWin() {
    }

    /**
     * Method is splitting the given LinkedList of Debitors/Kreditors from CobasWin
     * into NEW and UPDATE Debitors
     *
     * @param p_elementstosplitt the List with Kreditors or Debitors to splitt (Elements in List = VoksRecord)
     * @param p_compareelements the List with all Kreditors/Debitors from CobasWin for comparison
     * @return LinkedList[] 0 = New Debitors/Kreditors (to insert) 1 = Update Debitors/Kreditors (for update)
     */
    public List[] splitIntoUpdateAndInsert(List p_elementstosplitt, List p_compareelements){

        // local variables
        LinkedList[] r_splittarray;
        LinkedList _new_elements;
        LinkedList _update_elements;

        //init
        r_splittarray = new LinkedList[2];
        _new_elements = new LinkedList();
        _update_elements = new LinkedList();
        r_splittarray[0] = _new_elements;
        r_splittarray[1] = _update_elements;

        for (Iterator enum = p_elementstosplitt.iterator(); enum.hasNext();){

            VoksRecord _tmprecord;
            boolean _found;

            _tmprecord = (VoksRecord) enum.next();
            _found = false;


            if(_tmprecord.getKundenNr().equalsIgnoreCase("")){

                // Kreditor detected
                for (Iterator it = p_compareelements.iterator(); it.hasNext();){

                    VoksRecord _comparerecord;
                    _comparerecord = (VoksRecord) it.next();

                    if(_tmprecord.getLieferantenNr().equalsIgnoreCase(_comparerecord.getLieferantenNr())){

                        //record found in List adding to
                        //adding to update list
                        _update_elements.add(_tmprecord);

                        _found = true;
                        break;
                    }

                }

                if (_found == true){
                    //found and added to update list
                    //doining nothing
                    _found = false;
                }
                else{
                    //element not found, must be NEW
                    //adding to new element list
                    _new_elements.add(_tmprecord);
                }

            }
            else{

                //Debitor detected
                for (Iterator it = p_compareelements.iterator(); it.hasNext();){

                    VoksRecord _comparerecord;
                    _comparerecord = (VoksRecord) it.next();

                    if(_tmprecord.getKundenNr().equalsIgnoreCase(_comparerecord.getKundenNr())){

                        //record found in List adding to
                        //adding to update list
                        _update_elements.add(_tmprecord);

                        _found = true;
                        break;
                    }

                }

                if (_found == true){
                    //found and added to update list
                    //doining nothing
                    _found = false;
                }
                else{
                    //element not found, must be NEW
                    //adding to new element list
                    _new_elements.add(_tmprecord);
                }
            }
        }

        ApplicationLogger.getInstance().getLogger().info("SplitList for NEW Elements Size = " + _new_elements.size());
        ApplicationLogger.getInstance().getLogger().info("SplitList for UPDATE Elements Size = " + _update_elements.size());

        return r_splittarray;



    }
}
