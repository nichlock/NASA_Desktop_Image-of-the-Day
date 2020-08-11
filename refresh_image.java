
/**
 * This Java file gets the location of the NASA image of the day and downloads it to a new file either labeled img.jpg or img2.jpg
 */

// Getting the image and processing it
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.imageio.ImageIO;

// Reading XML documents
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// Printing  errors without a console
import javax.swing.JOptionPane;

public class refresh_image {

	static String nasa_iotd_rss = "https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss";

	/**
	 * Saves images grabbed from RSS feed.
	 * 
	 * @param First argument is the location to save the images. If no argument is
	 *              supplied, it saves to local directory
	 */
	public static void main(String args[]) {
		// Set the image to the one pulled from NASA's RSS feed
		if (args.length == 1)
			saveImage(parseXml(), args[0]); // Use given directory
		else
			saveImage(parseXml(), ""); // Use local directory
	}

	/**
	 * Get the image location form the XML format RSS feed
	 * 
	 * @return String format URL for the image to download
	 */
	private static String[] parseXml() {
		try {
			// Get and prepare the XML document
			DocumentBuilder dBuilder = (DocumentBuilderFactory.newInstance()).newDocumentBuilder();
			Document doc = dBuilder.parse((new URL(nasa_iotd_rss).openConnection()).getInputStream());
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("item");
			Node nNode = nList.item(0);

			// Get the link to the image and return it
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				// Get the image URL
				String image_url = eElement.getElementsByTagName("enclosure").item(0).getAttributes()
						.getNamedItem("url") + "";
				// Get the image format, which is in written in a more standard way than just
				// the end of URL
				String format = eElement.getElementsByTagName("enclosure").item(0).getAttributes().getNamedItem("type")
						+ "";
				// Trim to get only the values
				image_url = image_url.replace("url=\"", "").replace("\"", "").trim();
				format = format.replace("type=", "").replace("image/", "").replace("\"", "").trim();
				// Pack into an array for saveImage
				String[] str_arr = { image_url, format };
				return str_arr;
			}
//			return imagelink;

		} catch (Exception ex) { // If the RSS can't be accessed then open a window to warn the user
			JOptionPane.showMessageDialog(null,
					"Error while getting RSS.\nPerhaps try checking your internet connection?");
			System.exit(0);
		}
		return null; // Program should never get here, but this is needed for Java to compile

	}

	/**
	 * This saves the image to the specified location
	 * 
	 * @param imageURL  The URL to the image
	 * @param directory Where to save images
	 */
	public static void saveImage(String image[], String directory) {
		// Get the URL and type first
		String image_url = image[0];
		String image_format = image[1];
		// Get the image from the URL, and set it to the image files
		try {
			// Convert from http to https and get the URL object
			URL url = new URL(image_url.substring(0, 4) + "s" + image_url.substring(4));

			// Print some troubleshooting info
			System.out.println("Image URL: " + url.toString());
			System.out.println("Saving image from URL to " + directory + "img." + image_format);
			// Get the image
			BufferedImage iotd = null;
			iotd = ImageIO.read(url);
			// Write to the two image files.
			// Using two image files for Windows 7 compatibility
			ImageIO.write(iotd, image_format, new File(directory + "img." + image_format));
			ImageIO.write(iotd, image_format, new File(directory + "img2." + image_format));

		} catch (FileNotFoundException e) { // Could not write to file
			JOptionPane.showMessageDialog(null,
					"Error while writing to the image.\nCould not write to image at location: " + directory + "img.*");
			e.printStackTrace();
		} catch (Exception e) { // If getting the image failed, show why
			JOptionPane.showMessageDialog(null, "Error while writing to the image.");
			e.printStackTrace();
		}
	}

}
