import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class Main {

	/**
	 * Metodo que incia el programa
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// String link = "http://www.rae.es/";
		// String link = "https://as.com";
		String link = "https://www.marca.com";
		// String link = "https://museumis.github.io/Si/";
		// String link = "https://museumis.github.io/Si/java.html";

		try {
			GestionUrl gestionUrl = new GestionUrl(link);

			gestionUrl.informacionUrl();
			gestionUrl.obtenerTituloDeLaPagina();
			// gestionUrl.obtenerDiezLinkDeLaPagina();
			// gestionUrl.navegar();
			gestionUrl.obtenerImagen();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
