/**
 * This Java file gets the location of the NASA image of the day and downloads it to a new file either labeled img.jpg or img2.jpg
 */

// Getting the image and processing it
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.Color;

// Reading XML documents
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// Printing  errors without a console
import javax.swing.JOptionPane;

public class iotd_refresh {

	static String nasa_iotd_rss = "https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss";

	/**
	 * Saves images grabbed from RSS feed.
	 * 
	 * @param First argument is the location to save the images. If no argument is
	 *              supplied, it saves to local directory
	 */
	public static void main(String args[]) {
		// Set the image to the one pulled from NASA's RSS feed
		if (args.length > 0)
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
	 * This saves the image to the specified location *
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
			System.out.println("Saving image from URL to " + directory + "img.jpg");
			// Get the image in whatever format it was received in
			BufferedImage iotd = null;
			iotd = ImageIO.read(url);
			// If the format is not a jpeg, the colors need fixing
			if (image_format != "jpg" && image_format != "jpeg") {
				// Recreates buffered image with a new color format
				iotd = convertToJpg(iotd);
			}
			// Write to the two image files.
			// Using two image files for Windows 7 compatibility
			ImageIO.write(iotd, "jpg", new File(directory + "img.jpg"));
			ImageIO.write(iotd, "jpg", new File(directory + "img2.jpg"));
			// Could not write to file.
			// Usually this gets 'skipped' and NullPointerException is called instead
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Couldn't write to the image location: " + directory + "img.*");
			e.printStackTrace();
		} catch (NullPointerException e) { // This will also get thrown if the file was invalid
			JOptionPane.showMessageDialog(null,
					"Got a NullPointerException; please check the file path:\n" + directory + "img.*");
			e.printStackTrace();
		} catch (Exception e) { // If getting the image failed, show why
			JOptionPane.showMessageDialog(null, "Error while writing to the image:\n" + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Converts the given image to a jpeg color format.
	 */
	public static BufferedImage convertToJpg(BufferedImage orig) {
		// Create a blank BufferedImage with the correct colors
		BufferedImage newImage = new BufferedImage(orig.getWidth(), orig.getHeight(), BufferedImage.TYPE_INT_RGB);
		newImage.createGraphics().drawImage(orig, 0, 0, Color.WHITE, null); // Fill the new image
		return newImage;

	}

}
