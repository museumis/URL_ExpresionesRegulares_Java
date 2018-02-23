import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestionUrl {

	public String html, link;
	public URL url;
	public BufferedReader flujoEntrada;

	/**
	 * Constructor de la clase
	 * 
	 * @param link
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public GestionUrl(String link) throws UnsupportedEncodingException, IOException {

		this.link = link;
		crearHtml();

	}

	public void crearHtml() throws UnsupportedEncodingException, IOException {
		// Creacion url
		url = new URL(link);
		System.out.print("Web: " + link);

		// Optencion html// Obtener html en un solo String !!
		flujoEntrada = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

		html = "";// Texto a tratar con Expresiones regulares
		String linea = flujoEntrada.readLine();
		while (linea != null) {
			html += linea;
			linea = flujoEntrada.readLine();
		}
	}

	public void informacionUrl() {
		try {
			url = new URL(link);
			System.out.println("\n..............................");
			System.out.println("Protocolo -> " + url.getProtocol());
			System.out.println("Máquina -> " + url.getHost());
			System.out.println("Puerto -> " + url.getPort());
			System.out.println("Fichero -> " + url.getFile());// No saca cueiry
			System.out.println("User -> " + url.getUserInfo());
			System.out.println("Path -> " + url.getPath());
			System.out.println("Autoriadad -> " + url.getAuthority());
			System.out.println("Query -> " + url.getQuery());
			System.out.println("..............................");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void obtenerTituloDeLaPagina() {

		String titulo = "";
		String expresion = "<title.*?>(.*)<\\/title>";

		Pattern patron = Pattern.compile(expresion);
		Matcher matcher = patron.matcher(html);

		// Si existe patron
		if (matcher.find()) {
			titulo = matcher.group(1);
		}

		System.out.println(" -> Título -> " + titulo);
	}

	public void obtenerDiezLinkDeLaPagina() throws UnsupportedEncodingException, IOException {

		// ----------------------------------- PRIMEROS 10 LINK DE LA PAGINA
		ArrayList<String> vinculos = new ArrayList<>();

		String hipervinculo = "";
		String expresionLink = "<a href=\"((https|http).*?)\".*?>(.*?)<\\/a>";

		Pattern patronLink = Pattern.compile(expresionLink);
		Matcher matcherLin = patronLink.matcher(html);
		for (int i = 0; i < 10; i++) {
			if (matcherLin.find()) {
				hipervinculo = matcherLin.group(1);
				vinculos.add(hipervinculo);
			}
			System.out.println("Link " + (i + 1) + " -> " + hipervinculo);
		}
	}

	public void navegar() throws UnsupportedEncodingException, IOException {

		// ----------------------------------- PRIMEROS 10 LINK DE LA PAGINA
		ArrayList<String> vinculos = new ArrayList<>();

		String hipervinculo = "";
		String expresionLink = "<a href=\"((https|http).*?)\".*?>(.*?)<\\/a>";
		Pattern patronLink = Pattern.compile(expresionLink);
		Matcher matcherLin = patronLink.matcher(html);
		for (int i = 0; i < 10; i++) {
			if (matcherLin.find()) {
				hipervinculo = matcherLin.group(1);
				vinculos.add(hipervinculo);
			}
			System.out.println("Link " + (i + 1) + " -> " + hipervinculo);
		}
		// ------------------------------------ DAR OPCION A NAVEGAR POR LAS PAGINAS
		BufferedReader flujoEntrada;
		int opcion = 0;
		Scanner entrada;

		while ((opcion >= 0) && (opcion <= 10)) {

			// Se obtiene la opcion
			System.out.print("\n\tPróxima : ");
			entrada = new Scanner(System.in);
			opcion = entrada.nextInt() - 1;
			System.out.println("Cargando -> " + vinculos.get(opcion));

			// Se crea la nueva url de la opcion seleccionada
			url = new URL(vinculos.get(opcion));
			flujoEntrada = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String htmlAux = "";// Se crea el html nuevo
			String linea = flujoEntrada.readLine();
			while (linea != null) {
				htmlAux += linea;
				linea = flujoEntrada.readLine();
			}
			vinculos = new ArrayList<>();// Nueva lista de vinculos y anadir
			patronLink = Pattern.compile(expresionLink);
			matcherLin = patronLink.matcher(htmlAux);
			for (int i = 0; i < 10; i++) {
				if (matcherLin.find()) {
					hipervinculo = matcherLin.group(1);
					vinculos.add(hipervinculo);
				}
				System.out.println("Link " + (i + 1) + " -> " + hipervinculo);
			}
		}
	}

	public void obtenerImagen() throws UnsupportedEncodingException, IOException {

		// ----------------------------------- PRIMEROS 10 IMAGENES DE LA PAGINA
		ArrayList<String> imagenes = new ArrayList<>();

		String hipervinculo = "";
		String expresionLink = "<img src=\"(.*?)\" .*?\\/>";

		Pattern patronLink = Pattern.compile(expresionLink);
		Matcher matcherLin = patronLink.matcher(html);

		for (int i = 0; i < 10; i++) {
			if (matcherLin.find()) {
				hipervinculo = matcherLin.group(1);
				imagenes.add(hipervinculo);
			}
			System.out.println("Imagen " + (i + 1) + " -> " + hipervinculo);

		}

		// -------------------------------------------------DESCARGAR IMAGEN
		// Se obtiene la opcion
		int opcion = 0;
		// Opcion deseada
		System.out.print("\n\tDescargar : ");
		opcion = new Scanner(System.in).nextInt() - 1;
		url = new URL(imagenes.get(opcion));
		
		// Descargar
		InputStream flujoEntrada = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream flujoSalida = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int ncontador = 0;

		while (-1 != (ncontador = flujoEntrada.read(buffer))) {
			flujoSalida.write(buffer, 0, ncontador);
		}

		flujoSalida.close();
		flujoEntrada.close();

		byte[] imagen = flujoSalida.toByteArray();

		// Guardar
		FileOutputStream fos = new FileOutputStream("descargas//");
		fos.write(imagen);
		fos.close();

	}

}
