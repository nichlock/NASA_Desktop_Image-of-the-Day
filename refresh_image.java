//This Java file gets the location of the NASA image of the day and downloads it to a new file either labeled img.jpg or img2.jpg
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Main {

    public static void main(String args[]) { // This is the first section of code to run
    	saveImage(parseXml()); // Set the image to the image pulled from NASA's RSS feed
    }
    
    private static String parseXml() // The second class to run, returns the image link pulled from the RSS feed
    {								 // This class was 
    	String imagelink = "";
        try 
        {
            URLConnection conn = new URL("https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss").openConnection();
            DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc=dBuilder.parse(conn.getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nList=doc.getElementsByTagName("item");
            Node nNode=nList.item(0);
          

                    if(nNode.getNodeType()==Node.ELEMENT_NODE)
                    {
                        Element eElement=(Element) nNode;
                        String temp=eElement.getElementsByTagName("enclosure").item(0).getAttributes().getNamedItem("url")+"";
                        imagelink=temp.replace("url=\"", "").replace("\"", "").trim();
                    }
                    return imagelink;
            
            
        } catch (Exception ex) { //If the RSS can't be accessed then open a window to warn the user
            JOptionPane.showMessageDialog(null, "Error while getting RSS.\nPerhaps try checking your internet connection?");
            return null;
        }
        
    }
    public static void saveImage(String imageURL) // The third class to run, this saves the image to the specified location
    {
    	// For troubleshooting:
        System.out.println(imageURL.substring(0,4) + "s" + imageURL.substring(4));
        try { //try the following code:
        	
            // Get the image from the URL, and set it to the image files
            URL url = new URL(imageURL.substring(0,4) + "s" + imageURL.substring(4));
            BufferedImage imageS = null;
            imageS = ImageIO.read(url);
            ImageIO.write(imageS, "jpg", new File("img.jpg")); //writes to two image files, this allows for Windows 7 compatibility
            ImageIO.write(imageS, "jpg", new File("img2.jpg"));
            
        } catch (Exception ex) { // If the previous code failed for some reason, then open a windows that states the failure
            JOptionPane.showMessageDialog(null, "Error while writing to the image.");
            ex.printStackTrace();
        }
    }

}
