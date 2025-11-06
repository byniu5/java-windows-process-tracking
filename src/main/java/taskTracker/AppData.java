package taskTracker;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class AppData {
    
    private static AppData instance;
    private static final String DATA_FILE = "tasktracker_data.dat";
    private static final String ICONS_DIR = "tasktracker_icons";
    
    private List<TrackedApp> trackedApps;
    
    private AppData() {
        trackedApps = new ArrayList<>();
        ensureDataDirectoryExists();
    }
    
    public static AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }
    
    /**
     * Represents a single tracked application with all its data
     */
    public static class TrackedApp implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String title;
        private String path;
        private int timeInSeconds;
        private String iconFileName; // Reference to saved icon file

        public TrackedApp(String title, String path, int timeInSeconds, String iconFileName) {
            this.title = title;
            this.path = path;
            this.timeInSeconds = timeInSeconds;
            this.iconFileName = iconFileName;
        }
        
        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        
        public int getTimeInSeconds() { return timeInSeconds; }
        public void setTimeInSeconds(int timeInSeconds) { this.timeInSeconds = timeInSeconds; }
        
        public String getIconFileName() { return iconFileName; }
        public void setIconFileName(String iconFileName) { this.iconFileName = iconFileName; }
    }
    
    /**
     * Ensures the data directory and icons directory exist
     */
    private void ensureDataDirectoryExists() {
        try {
            Path iconsPath = Paths.get(ICONS_DIR);
            if (!Files.exists(iconsPath)) {
                Files.createDirectories(iconsPath);
            }
        } catch (IOException e) {
            System.err.println("Error creating data directories: " + e.getMessage());
        }
    }
    
    /**
     * Adds or updates a tracked application
     */
    public void addOrUpdateApp(String title, String path, int timeInSeconds, ImageIcon icon) {
        TrackedApp existing = findAppByPath(path);
        
        if (existing != null) {
            existing.setTimeInSeconds(timeInSeconds);
            existing.setTitle(title);
        } else {
            String iconFileName = null;
            if (icon != null) {
                iconFileName = saveIcon(icon, path);
            }
            TrackedApp newApp = new TrackedApp(title, path, timeInSeconds, iconFileName);
            trackedApps.add(newApp);
        }
    }

    /**
     * Finds a tracked app by its path
     */
    private TrackedApp findAppByPath(String path) {
        return trackedApps.stream()
                .filter(app -> app.getPath().equals(path))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Gets all tracked applications
     */
    public List<TrackedApp> getAllApps() {
        return new ArrayList<>(trackedApps);
    }
    
    /**
     * Saves icon to file and returns the filename
     */
    private String saveIcon(ImageIcon icon, String appPath) {
        try {
            // Create a unique filename based on the app path hash
            String fileName = "icon_" + Math.abs(appPath.hashCode()) + ".png";
            Path iconPath = Paths.get(ICONS_DIR, fileName);
            
            // Convert ImageIcon to BufferedImage and save
            java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    java.awt.image.BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics g = bi.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            
            javax.imageio.ImageIO.write(bi, "png", iconPath.toFile());
            return fileName;
        } catch (IOException e) {
            System.err.println("Error saving icon: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Loads an icon from file
     */
    public ImageIcon loadIcon(String iconFileName) {
        if (iconFileName == null) {
            return null;
        }
        
        try {
            Path iconPath = Paths.get(ICONS_DIR, iconFileName);
            if (Files.exists(iconPath)) {
                return new ImageIcon(iconPath.toString());
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Saves all tracked apps data to file
     */
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(trackedApps);
            System.out.println("Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads all tracked apps data from file
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File dataFile = new File(DATA_FILE);
        if (!dataFile.exists()) {
            System.out.println("No existing data file found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            trackedApps = (List<TrackedApp>) ois.readObject();
            System.out.println("Data loaded successfully from " + DATA_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
            trackedApps = new ArrayList<>();
        }
    }
    
    /**
     * Clears all tracked data (use with caution)
     */
    public void clearAllData() {
        trackedApps.clear();
        // Optionally delete the data file and icons
        try {
            Files.deleteIfExists(Paths.get(DATA_FILE));
            // Clean up icon files
            Path iconsPath = Paths.get(ICONS_DIR);
            if (Files.exists(iconsPath)) {
                Files.walk(iconsPath)
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("Error deleting icon: " + e.getMessage());
                            }
                        });
            }
        } catch (IOException e) {
            System.err.println("Error clearing data: " + e.getMessage());
        }
    }
    
    /**
     * Gets the total time tracked for a specific app path
     */
    public int getTimeForApp(String path) {
        TrackedApp app = findAppByPath(path);
        return app != null ? app.getTimeInSeconds() : 0;
    }
}
