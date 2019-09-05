package coms309.sb_c_4_cydisc;
/**
 * @author Brian Newman
 */

import android.os.Bundle;

/**
 * Created by Brian Newman on 11/8/2017.
 */

/**
 * This interface helps transfer data between the different fragments.
 */
public interface OnDataPass {

    /**
     * Passes a bundle of data from a fragment to the main activity.
     * @param data the bundle of data to pass.
     */
    public void onDataPass(Bundle data);

    /**
     * Gets the username retrieved from login.
     * @return the username from login.
     */
    public String getUsername();

    /**
     * Gets the playerid retrieved from login.
     * @return the player id from login.
     */
    public String getPlayerId();
}