package it.cascino;

import it.cascino.dao.ArticoliDao;
import it.cascino.dao.MyartmagDao;
import it.cascino.dao.MyprecodDao;
import it.cascino.dao.ProduttoriDao;
import it.cascino.mng.ArticoliDaoMng;
import it.cascino.mng.MyartmagDaoMng;
import it.cascino.mng.MyprecodDaoMng;
import it.cascino.mng.ProduttoriDaoMng;
import it.cascino.model.Articoli;
import it.cascino.model.Myartmag;
import it.cascino.model.Myprecod;
import it.cascino.model.Produttori;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
// import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RunTime{
	private final static String dirFoto = "c:\\dev\\foto";
	private final static String dirFotoTime = "t:";
	
	private static ArticoliDao articoliDao = new ArticoliDaoMng();
	// private static List<Articoli> articoliLs;
	// private static Articoli articoliAry[];
	
	private static ProduttoriDao produttoriDao = new ProduttoriDaoMng();
	private static List<Produttori> produttoriLs;
	private static Produttori produttoriAry[];
	private static int prodSizePad = 0;
	private static Map<Integer, String> produttoriMap = new HashMap<Integer, String>();
	
	private static MyartmagDao myartmagDao = new MyartmagDaoMng();
	private static List<Myartmag> myartmagLs;
	private static Myartmag myartmagAry[];
	private static Myartmag myartmagAryAppoggio[];
	
	private static MyprecodDao myprecodDao = new MyprecodDaoMng();
	
	private static List<Articoli> artInPostgresNonMyartmagLs;
	
	private static File fotoDaCancellareAry[] = null;
	
	static Logger log;
	
	public static void main(String args[]){
		PropertyConfigurator.configure("logdir/logRunTime.properties");
		log = Logger.getLogger(RunTime.class);
		
		// log.debug("Test Livello DEBUG");
		// log.info("Test Livello INFO");
		// log.warn("Test Livello WARNING");
		// log.error("Test Livello ERROR");
		// log.fatal("Test Livello FATAL");
		
		myartmagDao.resettaTabella();
		
		myprecodDao.svuotaTabella();
		produttoriLs = produttoriDao.getAll();
		produttoriAry = produttoriLs.toArray(new Produttori[produttoriLs.size()]);
		prodSizePad = StringUtils.length(Integer.toString(produttoriAry.length));
		popolaMyprecod();
		
		myartmagLs = myartmagDao.getAll();
		// articoliLs = articoliDao.getAll();
		myartmagAry = myartmagLs.toArray(new Myartmag[myartmagLs.size()]);
		myartmagAryAppoggio = myartmagAry.clone();
		
		// artMyartmagLs = identificaArtInMyartmagNonPostgres();
		// artInPostgresNonMyartmagLs = identificaArtInPostgresNonMyartmag();
		
		// fotoDaCancellareAry = new LinkedList<File>(Arrays.asList(getFileCaricati()));
		fotoDaCancellareAry = getFileCaricati();
		
		Myartmag articoliTimeSenzaFoto[] = elaboraArtInMyartmag();
		// Iterator<Myartmag> iterArticoliTimeSenzaFoto = articoliTimeSenzaFoto.iterator();
		log.info("articolo in time senza foto");
		for(int i = 0; i < articoliTimeSenzaFoto.length; i++){
			Myartmag myart = articoliTimeSenzaFoto[i];
			if(myart == null){
				continue;
			}
			log.info("articolo in time: " + myart.getOarti());
		}
		
		log.info("foto da cancellare");
		for(int i = 0; i < fotoDaCancellareAry.length; i++){
			File fotoDaCan = fotoDaCancellareAry[i];
			if(fotoDaCan == null){
				continue;
			}
			log.info("file: " + fotoDaCan.getName());
			deleteFile(fotoDaCan);
		}
		
		articoliDao.close();
		produttoriDao.close();
		myartmagDao.close();
		myprecodDao.close();
	}
	
	static Myartmag[] elaboraArtInMyartmag(){
		log.info("start: " + "elaboraArtInMyartmag");
		
		// List<String> oartiLs = new ArrayList<String>();
		// List<String> oarti_xgrupLs = new ArrayList<String>();
		// List<String> cprec_dartiLs = new ArrayList<String>();
		for(int i = 0; i < myartmagAryAppoggio.length; i++){
			Myartmag myartAppoggio = myartmagAryAppoggio[i];
			
			Articoli artic = articoliDao.getArticoloDaCodice(myartmagAry[i].getOarti());
			if(myartAppoggio != null){
				myartmagAry[i].setOartiXgrup("0"); // non di appoggio
			}
			if(artic == null){
				// se e' un articolo in ingrosso ma non nelle foto, quindi non so ne' i fratelli e nemmeno la marca, continuo e basta
				continue;
			}
			String prodPrec = produttoriMap.get(artic.getProduttore());
			if((artic != null) && (prodPrec != null)){
				myartmagAry[i].setCprecDarti(prodPrec);
			}else{
				myartmagAry[i].setCprecDarti("1");
			}
			
			if(myartAppoggio == null){
				continue;
			}
			log.info("articolo in time: " + myartAppoggio.getOarti());
			
			// identifico la foto per articolo in analisi
			Integer idFoto = articoliDao.getFotoArticoloDaCodice(myartAppoggio.getOarti());
			
			// se non ha foto disponibile, continuo con l'articolo successivo
			if(idFoto == -1){
				log.warn("non ha foto disponibile");
				continue;
			}
			// quindi ha una foto
			
			String fotoSorgente = articoliDao.getFotoNameArticoloDaId(idFoto);
			log.info("foto: " + fotoSorgente + " (id: " + idFoto + ")");
			
			String fratelloMaggiore = null;
			if(true){// fratelloMaggiore == null){
				log.info("e' un fratello maggiore");
				fratelloMaggiore = myartAppoggio.getOarti();
				
				String estensioneFile = StringUtils.lowerCase(fotoSorgente.substring(fotoSorgente.lastIndexOf(".") + 1));
				// l'uinica estensione ammessa e' jpg
				boolean estensioneAmmessa = true;
				if(!(StringUtils.equals(estensioneFile, "jpg"))){
					log.warn("estensione non gestita (" + estensioneFile + "), file: " + fotoSorgente);
					estensioneAmmessa = false;
					estensioneFile = "jpg";
				}
				
				int ordineFoto = 0;
				String fotoDestinazione = fratelloMaggiore + "_" + ordineFoto + "_A_0_" + fratelloMaggiore + "." + estensioneFile;
				
				// rimuovo dalla lista delle foto da cancellare
				File fileDaCanc = new File(dirFotoTime, fotoDestinazione);
				log.info("file: " + fileDaCanc.getAbsolutePath());
				if(fileDaCanc.exists()){
					log.info("il file " + fileDaCanc.getAbsolutePath() + " deve rimanere");
					for(int j = 0; j < fotoDaCancellareAry.length; j++){
						if(fotoDaCancellareAry[j] == null){
							continue;
						}
						if(StringUtils.equals(fotoDaCancellareAry[j].getName(), fileDaCanc.getName())){
							fotoDaCancellareAry[j] = null;
							break;
						}
					}
				}
				
				copyFile(dirFoto, fotoSorgente, dirFotoTime, fotoDestinazione);
				if(!(estensioneAmmessa)){
					convertiInJpg(dirFotoTime, fotoDestinazione);
				}
				log.info("elaborato articolo maggiore " + myartAppoggio.getOarti() + " e quindi rimossa dalla lista l'articolo");
				// iterMyartmag.remove();
				myartmagAryAppoggio[i] = null;
			}
			
			// cerco se ha fratelli (compreso se stesso) che condividono la stessa foto
			List<Articoli> fratelliLs = articoliDao.getArticoloFratelliLsDaCodiceFoto(idFoto);
			Articoli fratelliAry[] = fratelliLs.toArray(new Articoli[fratelliLs.size()]);
			log.info("fratelli: " + fratelliAry.length);
			
			if(fratelliAry.length == 1){ // e' solo lui
				continue;
			}
			
			// Iterator<Articoli> iterArticoli = fratelliLs.iterator();
			for(int j = 0; j < fratelliAry.length; j++){
				Articoli art = fratelliAry[j];
				log.info("articolo: " + art.getCodice() + " (id: " + art.getId() + ")");
				
				// controllo che effettivamente l'articolo abbia la foto come ordine minore e non come secondo o altro
				boolean ePrimaFoto = articoliDao.checkArticoloHaComePrimaFotoIdFoto(art, idFoto);
				
				if(!(ePrimaFoto)){
					log.info("non coincide con la foto di ordine minore (fratello scartato)");
					continue;
				}
				
				if(StringUtils.equals(fratelloMaggiore, art.getCodice())){
					log.info("continuo perche' e' lui stesso");
					continue; // con il fratello successivo
				}else{
					log.info("e' un fratello minore che deve ereditare la foto del fratello maggiore (" + fratelloMaggiore + ")");
					
					// myartmagDao.definisciFratelloMaggiore(art.getCodice(), fratelloMaggiore);
					// oartiLs.add(art.getCodice());
					// oarti_xgrupLs.add(fratelloMaggiore);
					
					log.info("elaborato articolo minore " + art.getCodice() + " e quindi rimossa dalla lista l'articolo");
					
					for(int y = 0; y < myartmagAryAppoggio.length; y++){
						Myartmag myartRem = myartmagAryAppoggio[y];
						if(myartRem == null){
							continue;
						}
						if(StringUtils.equals(myartRem.getOarti(), art.getCodice())){
							log.info("Rimossa: " + myartRem.getOarti() + ", " + myartRem.getOartiXgrup());
							myartmagAryAppoggio[y] = null;
							myartmagAry[y].setOartiXgrup(fratelloMaggiore);
						}
					}
				}
			}
		}
		
		// String oartiAry[] = oartiLs.toArray(new String[oartiLs.size()]);
		// String oarti_xgrupAry[] = oarti_xgrupLs.toArray(new String[oarti_xgrupLs.size()]);
		// String cprec_dartiAry[] = cprec_dartiLs.toArray(new String[cprec_dartiLs.size()]);
		// myartmagDao.aggiornaXgrupCprec(oartiAry, oarti_xgrupAry, cprec_dartiAry);
		myartmagDao.aggiornaXgrupCprec(myartmagAry);
		
		log.info("stop: " + "elaboraArtInMyartmag");
		// ritorna tutti gli articoli che non hanno comunque foto
		// return artMyartmagLs;
		// return new LinkedList<Myartmag>(Arrays.asList(myartmagAry));
		return myartmagAryAppoggio;
	}
	
	// restituisce la lista delle foto già caricate sulla cartella di time
	// utilizza unita' di rete mappata su T: === \\time.cascino.it\c$\MyMB\Archives\articoli_img\B2B\0
	public static File[] getFileCaricati(){
		File dirTime = new File(dirFotoTime);
		File fileGiaCaricati[] = dirTime.listFiles();
		
		for(File fC : fileGiaCaricati){
			log.info("Presente il file: " + fC.getName());
		}
		return fileGiaCaricati;
	}
	
	// static List<Myartmag> identificaArtInMyartmagNonPostgres(){
	// log.info("start: " + "identificaArtInMyartmagNonPostgres");
	// artMyartmagLs = myartmagLs;
	// Iterator<Myartmag> iterMyartmag = artMyartmagLs.iterator();
	// Iterator<Articoli> iterArticoli = articoliLs.iterator();
	// while(iterMyartmag.hasNext()){
	// Myartmag myart = iterMyartmag.next();
	// while(iterArticoli.hasNext()){
	// Articoli art = iterArticoli.next();
	// // se la trovo in postgresql, la tolgo
	// if(StringUtils.equals(myart.getOarti(), art.getCodice())){
	// // artMyartmagLs.remove(myart);
	// iterMyartmag.remove();
	// // iterMyartmag = artMyartmagLs.iterator();
	// log.info("Rimossa: " + myart.getOarti());
	// break;
	// }
	// }
	// }
	// log.info("stop: " + "identificaArtInMyartmagNonPostgres");
	// return artMyartmagLs;
	// }
	
	// static List<Articoli> identificaArtInPostgresNonMyartmag(){
	// log.info("start: " + "identificaArtInPostgresNonMyartmag");
	// artInPostgresNonMyartmagLs = articoliLs;
	// Iterator<Articoli> iterArticoli = artInPostgresNonMyartmagLs.iterator();
	// Iterator<Myartmag> iterMyartmag = myartmagLs.iterator();
	// while(iterArticoli.hasNext()){
	// Articoli art = iterArticoli.next();
	// while(iterMyartmag.hasNext()){
	// Myartmag myart = iterMyartmag.next();
	// // se la trovo in mysql, la tolgo
	// if(StringUtils.equals(art.getCodice(), myart.getOarti())){
	// // artInPostgresNonMyartmagLs.remove(art);
	// iterArticoli.remove();
	// // iterArticoli = artInPostgresNonMyartmagLs.iterator();
	// log.info("Rimossa: " + art.getCodice());
	// break;
	// }
	// }
	// }
	// log.info("stop: " + "identificaArtInPostgresNonMyartmag");
	// return artInPostgresNonMyartmagLs;
	// }
	
	public static void deleteFile(String path, String name){
		File file = null;
		if((path != null) && (name != null)){
			file = new File(path, name);
		}
		deleteFile(file);
	}
	
	public static void deleteFile(File file){
		System.gc();
		// log.info("file " + (file.canExecute()?"true":"false"));
		// log.info("file " + (file.canRead()?"true":"false"));
		// log.info("file " + (file.canWrite()?"true":"false"));
		if((file != null) && (Files.exists(file.toPath()))){
			// if(file.delete()){
			try{
				if(Files.deleteIfExists(file.toPath())){
					log.info("file " + file.getName() + " cancellato");
				}else{
					log.error("file " + file.getName() + " NON cancellato");
				}
			}catch(IOException e){
				log.error("file " + file.getName() + " gestito con eccezione");
				e.printStackTrace();
			}
		}
	}
	
	public static Path copyFile(String pathSource, String nameSouce, String pathDestination, String nameDestination){
		File fileSource = null;
		File fileDestination = null;
		if((pathSource != null) && (nameSouce != null)){
			fileSource = new File(pathSource, nameSouce);
		}
		if((pathDestination != null) && (nameDestination != null)){
			fileDestination = new File(pathDestination, nameDestination);
		}
		return copyFile(fileSource, fileDestination);
	}
	
	public static Path copyFile(File fileSource, File fileDestination){
		System.gc();
		Path targetFile = null;
		if((fileSource != null) && (Files.exists(fileSource.toPath())) && (fileDestination != null)){
			if(Files.notExists(fileDestination.toPath())){
				try{
					targetFile = Files.copy(fileSource.toPath(), fileDestination.toPath());
					if(Files.isSameFile(targetFile, fileDestination.toPath())){
						log.info("file " + fileSource.getName() + " copiato in " + fileDestination.getName());
					}else{
						log.error("file " + fileSource.getName() + " NON copiato in " + fileDestination.getName());
					}
				}catch(IOException e){
					log.error("file " + fileSource.getName() + " gestito con eccezione");
					e.printStackTrace();
				}
			}else{ // esiste gia' ma potrebbe essere che ha subito variazioni
				if((fileSource.length() == fileDestination.length()) && (StringUtils.equals(getResolution(fileSource), getResolution(fileDestination)))){
					log.info("file " + fileSource.getName() + " NON copiato in " + fileDestination.getName() + " (sono identici)");
				}else{
					// cancello il file
					try{
						log.info("file " + fileDestination.getName() + " cancellato");
						Files.delete(fileDestination.toPath());
					}catch(IOException e){
						log.error("file " + fileDestination.getName() + " cancellato gestito con eccezione");
						e.printStackTrace();
					}
					// chiamo ricorsivamente la copia
					targetFile = copyFile(fileSource, fileDestination);
				}
			}
		}
		return targetFile;
	}
	
	private static void convertiInJpg(String pathDestination, String nameDestination){
		File fileDestination = null;
		if((pathDestination != null) && (nameDestination != null)){
			fileDestination = new File(pathDestination, nameDestination);
		}
		
		BufferedImage bufferedImage;
		try{
			// read image file
			bufferedImage = ImageIO.read(fileDestination); // new File("c:\\javanullpointer.png"));
			
			// create a blank, RGB, same width and height, and a white background
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, java.awt.Color.WHITE, null);
			
			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", fileDestination);
		}catch(IOException e){
			log.error("file " + fileDestination.getName() + " conversione con eccezione");
			e.printStackTrace();
		}
	}
	
	private static String getResolution(File f){
		BufferedImage fimg = null;
		try{
			if((StringUtils.containsIgnoreCase(f.getPath(), "jpg")) || (StringUtils.containsIgnoreCase(f.getPath(), "jpeg"))){
				// gestisce i file jpeg, che se sono in formato CMYK, con ImageIO.read vanno in eccezione
				fimg = manageFileJpeg(f);
			}else{
				fimg = ImageIO.read(f);
			}
		}catch(IOException e){
			log.error("file " + f.getName() + " gestito con eccezione");
			e.printStackTrace();
		}
		return (fimg == null) ? "n.d." : fimg.getWidth() + "x" + fimg.getHeight() + "px";
	}
	
	private static BufferedImage manageFileJpeg(File f) throws IOException{
		Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
		ImageReader reader = null;
		while(readers.hasNext()){
			reader = (ImageReader)readers.next();
			if(reader.canReadRaster()){
				break;
			}
		}
		// Stream the image file (the original CMYK image)
		ImageInputStream input = null;
		input = ImageIO.createImageInputStream(f);
		reader.setInput(input);
		// Read the image raster
		Raster raster = null;
		raster = reader.readRaster(0, null);
		// Create a new RGB image
		BufferedImage bi = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		return bi;
	}
	
	static void popolaMyprecod(){
		log.info("start: " + "popolaMyprecod");
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		Timestamp currentTimestamp = new Timestamp(now.getTime());
		
		for(int i = 0; i < produttoriAry.length; i++){
			Produttori prod = produttoriAry[i];
			Myprecod precodice = new Myprecod("CASC", "1", currentTimestamp, "nome marca");
			String precodiceString = StringUtils.substring(prod.getNome(), 0, 4) + StringUtils.leftPad(Integer.toString(prod.getId()), prodSizePad, "0");
			precodice.setCprecDarti(precodiceString);
			precodice.setTprecDarti(prod.getNome());
			myprecodDao.salva(precodice);
			produttoriMap.put(prod.getId(), precodiceString);
		}
		log.info("stop: " + "popolaMyprecod");
	}
}
