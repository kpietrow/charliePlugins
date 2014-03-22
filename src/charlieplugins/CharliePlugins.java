/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package charlieplugins;

import charlie.advisor.BasicStrategy;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Devin's Work
 */
public class CharliePlugins {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("hello");
        
        Properties props = System.getProperties();
        props.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        props.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
        props.setProperty("org.slf4j.simpleLogger.showDateTime", "true");
        props.setProperty("org.slf4j.simpleLogger.dateTimeFormat","HH:mm:ss");
    
    Logger LOG = LoggerFactory.getLogger(CharliePlugins.class);
        LOG.info("hello");
    }
    
}
