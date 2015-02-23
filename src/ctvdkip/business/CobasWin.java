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
    public <E extends VoksRecord> List<List<E>> splitIntoUpdateAndInsert(List<E> p_elementstosplitt, List<E> p_compareelements){

        // local variables
        LinkedList<E> _new_elements;
        LinkedList<E> _update_elements;

        //init
        _new_elements = new LinkedList<E>();
        _update_elements = new LinkedList<E>();
        LinkedList<List<E>> tmpList = new LinkedList<List<E>>();
        tmpList.add(_new_elements);
        tmpList.add(_update_elements);

        for (Iterator<E> iterator = p_elementstosplitt.iterator(); iterator.hasNext();){

            E _tmprecord;
            boolean _found;

            _tmprecord = iterator.next();
            _found = false;


            if(_tmprecord.getKundenNr().equalsIgnoreCase("")){

                // Kreditor detected
                for (Iterator<E> it = p_compareelements.iterator(); it.hasNext();){

                    E _comparerecord;
                    _comparerecord = it.next();

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
                for (Iterator<E> it = p_compareelements.iterator(); it.hasNext();){

                    E _comparerecord;
                    _comparerecord = it.next();

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

        return tmpList;
    }
}
