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
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
	
	private AsMyfotxarDao asMyfotxarDao = new AsMyfotxarDaoMng();
	
	private AsAnmag0fDao asAnmag0fDao = new AsAnmag0fDaoMng();
	private List<AsAnmag0f> asAnmagLs;
	
	private AsAnmar0fDao asAnmar0fDao = new AsAnmar0fDaoMng();
	private List<AsAnmar0f> asAnmarLs;
	
	private File fotoDaCancellareAry[] = null;
	
	private Boolean fotoSorgenteModificataRecentemente;
	
	public Time(String args[]){
		log.info("[" + "Time");
		
		Date dataLimiteModifica = new Date(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataLimiteModifica);
		calendar.add(Calendar.MONTH, -2);
		dataLimiteModifica = calendar.getTime();
		
		// non posso prendere i soli MODIF=M, perché mi perderei le foto semplicemente sostituite e con lo stesso nome (e che quindi non hanno subito modifca in anmar0f)
		asAnmarLs = asAnmar0fDao.getAll();
		// asAnmarLs.clear();
		// asAnmarLs.add(asAnmar0fDao.getGruppoDaMcomp("10765"));
		
		fotoDaCancellareAry = getFileCaricati();
		
		asMyartmagLs = new ArrayList<AsMyartmag>();
		
		Iterator<AsAnmar0f> iter_asAnmar = asAnmarLs.iterator();
		while(iter_asAnmar.hasNext()){
			AsAnmar0f asAnmar0f = iter_asAnmar.next();
			log.info("****************** raggruppamento: " + asAnmar0f.getMcomp() + " - " + asAnmar0f.getMdes1());
			
			asAnmar0f.setModif(" ");
			asAnmar0fDao.aggiorna(asAnmar0f);
			
			if(StringUtils.isBlank(asAnmar0f.getMfoto())){
				log.warn(asAnmar0f.getMcomp() + " non ha nessuna foto definita");
				continue;
			}
			
			String fotoSorgente = StringUtils.trim(StringUtils.split(asAnmar0f.getMfoto(), ",")[0]);
			log.info("foto: " + fotoSorgente);
			
			fotoSorgenteModificataRecentemente = false;
			File fileFotoSorgenteModificataRecentemente = new File(dirFoto, fotoSorgente);
			if(fileFotoSorgenteModificataRecentemente.exists()){
				long ultimaMod = fileFotoSorgenteModificataRecentemente.lastModified();
				
				Path fileFotoSorgenteModificataRecentementePath = fileFotoSorgenteModificataRecentemente.toPath();
				BasicFileAttributes attributes = null;
				try{
					attributes = Files.readAttributes(fileFotoSorgenteModificataRecentementePath, BasicFileAttributes.class);
				}catch(IOException e){
					e.printStackTrace();
				}
				long timeAccess = attributes.lastAccessTime().to(TimeUnit.MILLISECONDS);
				long timeCreation = attributes.creationTime().to(TimeUnit.MILLISECONDS);
				long timeModif = attributes.lastModifiedTime().to(TimeUnit.MILLISECONDS);
				// prendo la piu' grande tra le 3, ovvero la piu' moderna
				ultimaMod = timeAccess;
				if(Long.compare(ultimaMod, timeCreation) < 0) {
					ultimaMod = timeCreation;
				}
				if(Long.compare(ultimaMod, timeModif) < 0) {
					ultimaMod = timeModif;
				}
				
				Date dataUltimaModifica = new Date(ultimaMod);
				calendar = Calendar.getInstance();
				calendar.setTime(dataUltimaModifica);
				dataUltimaModifica = calendar.getTime();
				
				if(dataUltimaModifica.compareTo(dataLimiteModifica) > 0){
					fotoSorgenteModificataRecentemente = true;
					log.info("foto recentemente modificata");
				}else{
					fotoSorgenteModificataRecentemente = false;
					log.info("foto non modificata di recente");
				}
			}else{
				log.error(fotoSorgente + " non e' presente");
				continue;
			}
			
			asAnmagLs = asAnmag0fDao.getArticoliDaMcompIngrosso(asAnmar0f.getMcomp());
			
			if(asAnmagLs.isEmpty()){
				log.info("raggruppamento " + asAnmar0f.getMcomp() + " non ha articoli all'ingrosso");
				continue;
			}
			
			log.info("gruppo di " + asAnmagLs.size());
			
			boolean estensioneAmmessa = true;
			if(fotoSorgenteModificataRecentemente){
				String estensioneFile = fotoSorgente.substring(fotoSorgente.lastIndexOf(".") + 1);
				// l'uinica estensione ammessa e' jpg
				if(!(StringUtils.equalsIgnoreCase(estensioneFile, "jpg"))){
					log.warn("estensione non gestita (" + estensioneFile + "), file: " + fotoSorgente);
					estensioneAmmessa = false;
					
					String fotoDest = StringUtils.replace(StringUtils.replace(fotoSorgente, StringUtils.lowerCase(estensioneFile), "jpg"), StringUtils.upperCase(estensioneFile), "jpg");
					copyFile(dirFoto, fotoSorgente, dirFoto, fotoDest);
					fotoSorgente = fotoDest;
					convertiInJpg(dirFoto, fotoSorgente);
					log.info("foto: " + fotoSorgente);
					estensioneFile = "jpg";
				}
			}
			
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
					
					fotoDestinazione = determinaNomeFotoDestinazione(fratelloMaggiore, fotoSorgente);
					
					// rimuovo dalla lista delle foto da cancellare
					File fileDaCanc = new File(dirFotoTime, fotoDestinazione);
					log.info("file: " + fileDaCanc.getAbsolutePath());
					if(fileDaCanc.exists()){
						log.info("il file " + fileDaCanc.getAbsolutePath() + " deve rimanere nella directory");
						for(int j = 0; j < fotoDaCancellareAry.length; j++){
							if(fotoDaCancellareAry[j] == null){
								continue;
							}
							if(StringUtils.equals(fotoDaCancellareAry[j].getName(), fileDaCanc.getName())){
								fotoDaCancellareAry[j] = null;
								break;
							}
						}
					}else{
						// se non esiste, in ogni caso va copiata in time
						log.info("La foto non esiste in Time, quindi la copio");
						fotoSorgenteModificataRecentemente = true;
					}
					
					if(fotoSorgenteModificataRecentemente){
						copyFile(dirFoto, fotoSorgente, dirFotoTime, fotoDestinazione);
						if(!(estensioneAmmessa)){
							File file = new File(dirFoto, fotoSorgente);
							deleteFile(file);
						}
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
				asMyfotxarDao.detach(asMyfotxar);
			}
			asAnmar0f.setModif(" ");
			asAnmar0fDao.aggiorna(asAnmar0f);
			asAnmar0fDao.detach(asAnmar0f);
		}
		
		asMyartmagAry = asMyartmagLs.toArray(new AsMyartmag[asMyartmagLs.size()]);
		
		asMyartmagDao.aggiornaXgrup(asMyartmagAry);
		// asMyartmagDao.detach(asMyartmagAry);
		
		log.info("foto da cancellare");
		for(int i = 0; i < fotoDaCancellareAry.length; i++){
			File fotoDaCan = fotoDaCancellareAry[i];
			if(fotoDaCan == null){
				continue;
			}
			log.info("file: " + fotoDaCan.getName());
			deleteFile(fotoDaCan);
		}
		
		asAnmar0fDao.close();
		asAnmag0fDao.close();
		asMyartmagDao.close();
		asMyfotxarDao.close();
		
		log.info("]" + "Time");
	}
	
	// restituisce la lista delle foto già caricate sulla cartella di time
	// utilizza unita' di rete mappata su T: === \\time.cascino.it\c$\MyMB\Archives\articoli_img\B2B\0
	private File[] getFileCaricati(){
		log.info("[" + "getFileCaricati");
		File dirTime = new File(dirFotoTime);
		File fileGiaCaricati[] = dirTime.listFiles();
		
		log.info("Presenti " + fileGiaCaricati.length + " foto");
		
		for(File fC : fileGiaCaricati){
			log.info("Presente il file: " + fC.getName());
		}
		log.info("]" + "getFileCaricati");
		return fileGiaCaricati;
	}
	
	private void deleteFile(File file){
		log.info("[" + "deleteFile");
		System.gc();
		// log.info("file " + (file.canExecute()?"true":"false"));
		// log.info("file " + (file.canRead()?"true":"false"));
		// log.info("file " + (file.canWrite()?"true":"false"));
		if((file != null) && (Files.exists(file.toPath()))){
			// if(file.delete()){
			try{
				if(Files.deleteIfExists(file.toPath())){
					log.info("file " + file.getAbsolutePath() + " cancellato");
				}else{
					log.error("file " + file.getAbsolutePath() + " NON cancellato");
				}
			}catch(IOException e){
				log.error("file " + file.getAbsolutePath() + " gestito con eccezione (in cancellazione)");
				e.printStackTrace();
			}
		}
		log.info("]" + "deleteFile");
	}
	
	private Path copyFile(String pathSource, String nameSource, String pathDestination, String nameDestination){
		log.info("[" + "copyFile (4 parametri)");
		File fileSource = null;
		File fileDestination = null;
		if((pathSource != null) && (nameSource != null)){
			fileSource = new File(pathSource, nameSource);
		}
		if((pathDestination != null) && (nameDestination != null)){
			fileDestination = new File(pathDestination, nameDestination);
		}
		log.info("]" + "copyFile (4 parametri)");
		return copyFile(fileSource, fileDestination);
	}
	
	private Path copyFile(File fileSource, File fileDestination){
		log.info("[" + "copyFile (2 parametri)");
		System.gc();
		Path targetFile = null;
		if((fileSource != null) && (Files.exists(fileSource.toPath())) && (fileDestination != null)){
			if(Files.notExists(fileDestination.toPath())){
				try{
					targetFile = Files.copy(fileSource.toPath(), fileDestination.toPath());
					if(Files.isSameFile(targetFile, fileDestination.toPath())){
						log.info("file " + fileSource.getAbsolutePath() + " copiato in " + fileDestination.getAbsolutePath());
					}else{
						log.error("file " + fileSource.getAbsolutePath() + " NON copiato in " + fileDestination.getAbsolutePath());
					}
				}catch(IOException e){
					log.error("file " + fileSource.getAbsolutePath() + " gestito con eccezione (in copia)");
					e.printStackTrace();
				}
			}else{ // esiste gia' ma potrebbe essere che ha subito variazioni
				if((fileSource.length() == fileDestination.length()) && (StringUtils.equals(getResolution(fileSource), getResolution(fileDestination)))){
					log.info("file " + fileSource.getName() + " NON copiato in " + fileDestination.getName() + " (sono identici)");
				}else{
					// cancello il file
					try{
						log.info("file " + fileDestination.getAbsolutePath() + " cancellato");
						Files.delete(fileDestination.toPath());
					}catch(IOException e){
						log.error("file " + fileDestination.getAbsolutePath() + " cancellato gestito con eccezione (in cancellazione)");
						e.printStackTrace();
					}
					// chiamo ricorsivamente la copia
					targetFile = copyFile(fileSource, fileDestination);
				}
			}
		}else{
			log.warn("file " + fileSource.getAbsolutePath() + " NON esiste");
		}
		log.info("]" + "copyFile (2 parametri)");
		return targetFile;
	}
	
	private void convertiInJpg(String pathDestination, String nameDestination){
		log.info("[" + "convertiInJpg");
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
		log.info("]" + "convertiInJpg");
	}
	
	private String getResolution(File f){
		// log.info("[" + "getResolution");
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
		// log.info("]" + "getResolution");
		return (fimg == null) ? "n.d." : fimg.getWidth() + "x" + fimg.getHeight() + "px";
	}
	
	private BufferedImage manageFileJpeg(File f) throws IOException{
		// log.info("[" + "manageFileJpeg");
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
		// log.info("]" + "manageFileJpeg");
		return bi;
	}
	
	private String determinaNomeFotoDestinazione(String fratelloMaggiore, String fotoSorgente){
		log.info("[" + "determinaNomeFotoDestinazione");
		
		String fotoDestinazione = "";
		
		// File dirTime = new File(dirFotoTime);
		// File fileGiaCaricati[] = dirTime.listFiles(new FilenameFilter(){
		// @Override
		// public boolean accept(File dir, String fileName){
		// boolean result;
		// if(StringUtils.startsWith(fileName, fratelloMaggiore + "_")){
		// result = true;
		// }else{
		// result = false;
		// }
		// return result;
		// }
		// });
		//
		// int ordineFoto = 0;
		// int versioneFoto = 0;
		//
		// if(fileGiaCaricati.length == 0){
		// fotoDestinazione = fratelloMaggiore + "_" + ordineFoto + "_A_" + versioneFoto + "_" + fratelloMaggiore + ".jpg";
		// }else if(fileGiaCaricati.length == 1){
		// fotoDestinazione = fileGiaCaricati[0].getName();
		// }else if(fileGiaCaricati.length > 1){
		// log.warn("esistono " + fileGiaCaricati.length + " foto con lo stesso prefisso: " + fratelloMaggiore + "*");
		// fotoDestinazione = fileGiaCaricati[0].getName();
		// }
		
		int ordineFoto = 0;
		int versioneFoto = 0;
		
		for(int i = 0; i < fotoDaCancellareAry.length; i++){
			if(fotoDaCancellareAry[i] == null){
				continue;
			}
			if(StringUtils.startsWith(fotoDaCancellareAry[i].getName(), fratelloMaggiore + "_")){
				fotoDestinazione = fotoDaCancellareAry[i].getName();
				break;
			}
		}
		// se non e' gia' presente
		if(StringUtils.isBlank(fotoDestinazione)){
			fotoDestinazione = fratelloMaggiore + "_" + ordineFoto + "_A_" + versioneFoto + "_" + fratelloMaggiore + ".jpg";
		}
		
		if(fotoSorgenteModificataRecentemente){
			File fileSource = new File(dirFoto, fotoSorgente);
			File fileDestination = new File(dirFotoTime, fotoDestinazione);
			
			if(fileDestination.exists()){
				if(!((fileSource.length() == fileDestination.length()) && (StringUtils.equals(getResolution(fileSource), getResolution(fileDestination))))){
					String strVersioneFoto = StringUtils.removeStart(fotoDestinazione, fratelloMaggiore + "_" + ordineFoto + "_A_");
					strVersioneFoto = StringUtils.removeEnd(strVersioneFoto, "_" + fratelloMaggiore + ".jpg");
					
					versioneFoto = Integer.parseInt(strVersioneFoto);
					versioneFoto = versioneFoto + 1;
					
					fotoDestinazione = fratelloMaggiore + "_" + ordineFoto + "_A_" + versioneFoto + "_" + fratelloMaggiore + ".jpg";
				}
			}
		}
		log.info("]" + "determinaNomeFotoDestinazione");
		return fotoDestinazione;
	}
}
