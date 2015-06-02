package swift.storage;

import swift.core.ScoutIdentity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class FileScoutIdentity implements ScoutIdentity {

    private final Properties props;
    private final String path;

    public FileScoutIdentity(String path) {
        this.path = path;
        props = new Properties();
        if (!load()) {
            setDefault();
            save();
        }
    }

    private boolean load() {
        try {
            props.load(new FileInputStream(path));
            return props.containsKey("name") && props.containsKey("counter");
        } catch (IOException e) {
            return false;
        }
    }

    private void setDefault() {
        props.setProperty("name", "client" + new Random().nextInt(16777216));
        props.setProperty("counter", "1");
    }

    private void trySave() throws IOException {
        // Ensure that the file exists
        File f = new File(path);
        if (!f.exists()) f.createNewFile();
        // Save
        props.store(new FileOutputStream(path), "Scout identity file");
    }

    private void save() {
        try {
            trySave();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String scoutName() {
        return props.getProperty("name");
    }

    @Override
    public synchronized long getNextTxnId() {
        long value = Long.parseLong(props.getProperty("counter"));
        props.setProperty("counter", "" + (value+1));
        save();
        return value;
    }
}
