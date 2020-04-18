package it.cascino.time;

import it.cascino.time.dbas.dao.AsAnmag0fDao;
import it.cascino.time.dbas.dao.AsAnmar0fDao;
import it.cascino.time.dbas.dao.AsMyartmagDao;
import it.cascino.time.dbas.dao.AsMyfotxarDao;
import it.cascino.time.dbas.managmentbean.AsAnmag0fDaoMng;
import it.cascino.time.dbas.managmentbean.AsAnmar0fDaoMng;
import it.cascino.time.dbas.managmentbean.AsMyartmagDaoMng;
import it.cascino.time.dbas.managmentbean.AsMyfotxarDaoMng;
import it.cascino.time.dbas.model.AsAnmag0f;
import it.cascino.time.dbas.model.AsAnmar0f;
import it.cascino.time.dbas.model.AsMyartmag;
import it.cascino.time.dbas.model.AsMyfotxar;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class Time{
	
	private Logger log = Logger.getLogger(Time.class);
	
	private final String dirFoto = "c:\\dev\\foto";
	private final String dirFotoTime = "t:";
	
	private AsMyartmagDao asMyartmagDao = new AsMyartmagDaoMng();
	private List<AsMyartmag> asMyartmagLs;
	private AsMyartmag asMyartmagAry[];
	//private MysMyartmag asMyartmagAryAppoggio[];
	
	private AsMyfotxarDao asMyfotxarDao = new AsMyfotxarDaoMng();
	
	private AsAnmag0fDao asAnmag0fDao = new AsAnmag0fDaoMng();
	private List<AsAnmag0f> asAnmagLs;

	private AsAnmar0fDao asAnmar0fDao = new AsAnmar0fDaoMng();
	private List<AsAnmar0f> asAnmarLs;

	private File fotoDaCancellareAry[] = null;
	
	public Time(String args[]){
		log.info("[" + "Time");
		
		asAnmarLs = asAnmar0fDao.getAll();
		
		fotoDaCancellareAry = getFileCaricati();
		
		asMyartmagLs = new ArrayList<AsMyartmag>();
		
		Iterator<AsAnmar0f> iter_asAnmar = asAnmarLs.iterator();
		while(iter_asAnmar.hasNext()){
			AsAnmar0f asAnmar0f = iter_asAnmar.next();
			log.info("raggruppamento: " + asAnmar0f.getMcomp() + " - " +  asAnmar0f.getMdes1());
			
			if(StringUtils.isEmpty(asAnmar0f.getMfoto())){
				log.warn(asAnmar0f.getMcomp() + " non ha nessuna foto definita");
				continue;
			}
			
			String fotoSorgente = StringUtils.trim(StringUtils.split(asAnmar0f.getMfoto(), ",")[0]);
			log.info("foto: " + fotoSorgente);
			
			String estensioneFile = StringUtils.lowerCase(fotoSorgente.substring(fotoSorgente.lastIndexOf(".") + 1));
			// l'uinica estensione ammessa e' jpg
			boolean estensioneAmmessa = true;
			if(!(StringUtils.equals(estensioneFile, "jpg"))){
				log.warn("estensione non gestita (" + estensioneFile + "), file: " + fotoSorgente);
				estensioneAmmessa = false;
				estensioneFile = "jpg";
			}
							
			asAnmagLs = asAnmag0fDao.getArticoliDaMcompIngrosso(asAnmar0f.getMcomp());
			
			if(asAnmagLs.isEmpty()){
				log.warn("raggruppamento " + asAnmar0f.getMcomp() + " non ha articoli all'ingrosso");
				continue;
			}
			
			log.info("gruppo di " + asAnmagLs.size());
			
			Iterator<AsAnmag0f> iter_asAnmag = asAnmagLs.iterator();
			Boolean soloPrimo = true;
			String fratelloMaggiore = null;
			String fotoDestinazione = null;
			while(iter_asAnmag.hasNext()){
				AsAnmag0f asAnmag0f = iter_asAnmag.next();
				
				AsMyartmag asMyartmag = asMyartmagDao.getDaOarti(asAnmag0f.getMcoda());
				if(asMyartmag == null){
					log.error(asAnmag0f.getMcoda() + " non presente in Myartmag");
					continue;
				}						
				
				if(soloPrimo){
					log.info("e' un fratello maggiore");
					fratelloMaggiore = StringUtils.trim(asMyartmag.getOarti());
					
					asMyartmag.setOartiXgrup("0");
					
					int ordineFoto = 0;
					fotoDestinazione = fratelloMaggiore + "_" + ordineFoto + "_A_0_" + fratelloMaggiore + "." + estensioneFile;
					
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

					soloPrimo = false;
				}else{
					log.info("e' un fratello minore che deve ereditare la foto del fratello maggiore (" + fratelloMaggiore + ")");
					
					asMyartmag.setOartiXgrup(fratelloMaggiore);					
				}
				
				asMyartmagLs.add(asMyartmag);
				
				// in myfotxar vanno tutti gli articoli, sia fratelli maggiori che minori 
				AsMyfotxar asMyfotxar = new AsMyfotxar();
				asMyfotxar.setCsoci("CASC");
				asMyfotxar.setCtipoDdocm("foto");
				asMyfotxar.setCreviDdocm("0");
				asMyfotxar.setOarti(asMyartmag.getOarti());
				asMyfotxar.setTfileDdocm(fotoDestinazione);
				asMyfotxarDao.salva(asMyfotxar);
			}
		}
		
		
		asMyartmagAry = asMyartmagLs.toArray(new AsMyartmag[asMyartmagLs.size()]);
		
		asMyartmagDao.aggiornaXgrup(asMyartmagAry);
		
		//	asMyartmagLs = asMyartmagDao.getAll();
		// articoliLs = articoliDao.getAll();
//		asMyartmagAry = asMyartmagLs.toArray(new MysMyartmag[asMyartmagLs.size()]);
//		asMyartmagAryAppoggio = asMyartmagAry.clone();
		
		// artMyartmagLs = identificaArtInMyartmagNonPostgres();
		// artInPostgresNonMyartmagLs = identificaArtInPostgresNonMyartmag();
		
		// fotoDaCancellareAry = new LinkedList<File>(Arrays.asList(getFileCaricati()));
		
		
//		MysMyartmag articoliTimeSenzaFoto[] = elaboraArtInMyartmag();
//		// Iterator<Myartmag> iterArticoliTimeSenzaFoto = articoliTimeSenzaFoto.iterator();
//		log.info("articolo in time senza foto");
//		for(int i = 0; i < articoliTimeSenzaFoto.length; i++){
//			MysMyartmag myart = articoliTimeSenzaFoto[i];
//			if(myart == null){
//				continue;
//			}
//			log.info("articolo in time: " + myart.getOarti());
//		}
		
		log.info("foto da cancellare");
		for(int i = 0; i < fotoDaCancellareAry.length; i++){
			File fotoDaCan = fotoDaCancellareAry[i];
			if(fotoDaCan == null){
				continue;
			}
			log.info("file: " + fotoDaCan.getName());
			deleteFile(fotoDaCan);
		}
		
		//pgArticoliDao.close();
		asAnmar0fDao.close();
		asAnmag0fDao.close();
		asMyartmagDao.close();
		asMyfotxarDao.close();
		
		log.info("]" + "Time");
	}
	
//	private MysMyartmag[] elaboraArtInMyartmag(){
//		log.info("[" + "elaboraArtInMyartmag");
//		
//		// List<String> oartiLs = new ArrayList<String>();
//		// List<String> oarti_xgrupLs = new ArrayList<String>();
//		// List<String> cprec_dartiLs = new ArrayList<String>();
//		for(int i = 0; i < asMyartmagAryAppoggio.length; i++){
//			MysMyartmag myartAppoggio = asMyartmagAryAppoggio[i];
//			
//			PgArticoli artic = pgArticoliDao.getArticoloDaCodice(asMyartmagAry[i].getOarti());
//			if(myartAppoggio != null){
//				asMyartmagAry[i].setOartiXgrup("0"); // non di appoggio
//			}
//			if(artic == null){
//				// se e' un articolo in ingrosso ma non nelle foto, quindi non so i fratelli, continuo e basta
//				continue;
//			}
//			
//			if(myartAppoggio == null){
//				continue;
//			}
//			log.info("articolo in time: " + myartAppoggio.getOarti());
//			
//			// identifico la foto per articolo in analisi
//			Integer idFoto = pgArticoliDao.getFotoArticoloDaCodice(myartAppoggio.getOarti());
//			
//			// se non ha foto disponibile, continuo con l'articolo successivo
//			if(idFoto == -1){
//				log.warn("non ha foto disponibile");
//				continue;
//			}
			// quindi ha una foto
			
//			String fotoSorgente = pgArticoliDao.getFotoNameArticoloDaId(idFoto);
//			log.info("foto: " + fotoSorgente + " (id: " + idFoto + ")");
//			
//			MysMyfotxar asMyfotxar = new MysMyfotxar();
//			asMyfotxar.setCsoci("CASC");
//			asMyfotxar.setCtipoDdocm("foto");
//			asMyfotxar.setCreviDdocm("0");
//			
//			String fratelloMaggiore = null;
//			String fotoDestinazione = null;
//			if(true){// fratelloMaggiore == null){
//				log.info("e' un fratello maggiore");
//				fratelloMaggiore = myartAppoggio.getOarti();
//				
//				String estensioneFile = StringUtils.lowerCase(fotoSorgente.substring(fotoSorgente.lastIndexOf(".") + 1));
//				// l'uinica estensione ammessa e' jpg
//				boolean estensioneAmmessa = true;
//				if(!(StringUtils.equals(estensioneFile, "jpg"))){
//					log.warn("estensione non gestita (" + estensioneFile + "), file: " + fotoSorgente);
//					estensioneAmmessa = false;
//					estensioneFile = "jpg";
//				}
//				
//				int ordineFoto = 0;
//				fotoDestinazione = fratelloMaggiore + "_" + ordineFoto + "_A_0_" + fratelloMaggiore + "." + estensioneFile;
//
//				asMyfotxar.setOarti(fratelloMaggiore);
//				asMyfotxar.setTfileDdocm(fotoDestinazione);
//				asMyfotxarDao.salva(asMyfotxar);
//
//				// rimuovo dalla lista delle foto da cancellare
//				File fileDaCanc = new File(dirFotoTime, fotoDestinazione);
//				log.info("file: " + fileDaCanc.getAbsolutePath());
//				if(fileDaCanc.exists()){
//					log.info("il file " + fileDaCanc.getAbsolutePath() + " deve rimanere");
//					for(int j = 0; j < fotoDaCancellareAry.length; j++){
//						if(fotoDaCancellareAry[j] == null){
//							continue;
//						}
//						if(StringUtils.equals(fotoDaCancellareAry[j].getName(), fileDaCanc.getName())){
//							fotoDaCancellareAry[j] = null;
//							break;
//						}
//					}
//				}
//				
//				copyFile(dirFoto, fotoSorgente, dirFotoTime, fotoDestinazione);
//				if(!(estensioneAmmessa)){
//					convertiInJpg(dirFotoTime, fotoDestinazione);
//				}
//				log.info("elaborato articolo maggiore " + myartAppoggio.getOarti() + " e quindi rimossa dalla lista l'articolo");
//				// iterMyartmag.remove();
//				asMyartmagAryAppoggio[i] = null;
//			}
//			
//			// cerco se ha fratelli (compreso se stesso) che condividono la stessa foto
//			List<PgArticoli> fratelliLs = pgArticoliDao.getArticoloFratelliLsDaCodiceFoto(idFoto);
//			PgArticoli fratelliAry[] = fratelliLs.toArray(new PgArticoli[fratelliLs.size()]);
//			log.info("fratelli: " + fratelliAry.length);
//			
//			if(fratelliAry.length == 1){ // e' solo lui
//				continue;
//			}
//			
//			// Iterator<Articoli> iterArticoli = fratelliLs.iterator();
//			for(int j = 0; j < fratelliAry.length; j++){
//				PgArticoli art = fratelliAry[j];
//				log.info("articolo: " + art.getCodice() + " (id: " + art.getId() + ")");
//				
//				// controllo che effettivamente l'articolo abbia la foto come ordine minore e non come secondo o altro
//				boolean ePrimaFoto = pgArticoliDao.checkArticoloHaComePrimaFotoIdFoto(art, idFoto);
//				
//				if(!(ePrimaFoto)){
//					log.info("non coincide con la foto di ordine minore (fratello scartato)");
//					continue;
//				}
//				
//				if(StringUtils.equals(fratelloMaggiore, art.getCodice())){
//					log.info("continuo perche' e' lui stesso");
//					continue; // con il fratello successivo
//				}else{
//					log.info("e' un fratello minore che deve ereditare la foto del fratello maggiore (" + fratelloMaggiore + ")");
//					
//					// myartmagDao.definisciFratelloMaggiore(art.getCodice(), fratelloMaggiore);
//					// oartiLs.add(art.getCodice());
//					// oarti_xgrupLs.add(fratelloMaggiore);
//
//					asMyfotxar = new MysMyfotxar();
//					asMyfotxar.setCsoci("CASC");
//					asMyfotxar.setCtipoDdocm("foto");
//					asMyfotxar.setCreviDdocm("0");
//					asMyfotxar.setOarti(art.getCodice());
//					asMyfotxar.setTfileDdocm(fotoDestinazione);
//
//					asMyfotxarDao.salva(asMyfotxar);
//					
//					log.info("elaborato articolo minore " + art.getCodice() + " e quindi rimossa dalla lista l'articolo");
//					
//					for(int y = 0; y < asMyartmagAryAppoggio.length; y++){
//						MysMyartmag myartRem = asMyartmagAryAppoggio[y];
//						if(myartRem == null){
//							continue;
//						}
//						if(StringUtils.equals(myartRem.getOarti(), art.getCodice())){
//							log.info("Rimossa: " + myartRem.getOarti() + ", " + myartRem.getOartiXgrup());
//							asMyartmagAryAppoggio[y] = null;
//							asMyartmagAry[y].setOartiXgrup(fratelloMaggiore);
//						}
//					}
//				}
//			}
//		}
		
		// String oartiAry[] = oartiLs.toArray(new String[oartiLs.size()]);
		// String oarti_xgrupAry[] = oarti_xgrupLs.toArray(new String[oarti_xgrupLs.size()]);
		// String cprec_dartiAry[] = cprec_dartiLs.toArray(new String[cprec_dartiLs.size()]);
		// myartmagDao.aggiornaXgrupCprec(oartiAry, oarti_xgrupAry, cprec_dartiAry);
//		asMyartmagDao.aggiornaXgrup(asMyartmagAry);
		
//		log.info("]" + "elaboraArtInMyartmag");
		// ritorna tutti gli articoli che non hanno comunque foto
		// return artMyartmagLs;
		// return new LinkedList<Myartmag>(Arrays.asList(myartmagAry));
//		return asMyartmagAryAppoggio;
//	}
	
	// restituisce la lista delle foto già caricate sulla cartella di time
	// utilizza unita' di rete mappata su T: === \\time.cascino.it\c$\MyMB\Archives\articoli_img\B2B\0
	private File[] getFileCaricati(){
		File dirTime = new File(dirFotoTime);
		File fileGiaCaricati[] = dirTime.listFiles();
		
		for(File fC : fileGiaCaricati){
			log.info("Presente il file: " + fC.getName());
		}
		return fileGiaCaricati;
	}
	
	private void deleteFile(File file){
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
	
	private Path copyFile(String pathSource, String nameSouce, String pathDestination, String nameDestination){
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
	
	private Path copyFile(File fileSource, File fileDestination){
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
	
	private void convertiInJpg(String pathDestination, String nameDestination){
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
	
	private String getResolution(File f){
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
	
	private BufferedImage manageFileJpeg(File f) throws IOException{
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
}
