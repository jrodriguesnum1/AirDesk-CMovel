package pt.ulisboa.tecnico.cmov.g15.airdesk.storage;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;

/**
 * Created by diogo on 03-04-2015.
 */
public class FileSystemManager {
    private static File sdcard0 = Environment.getExternalStorageDirectory();

    public static String createFile(String dirPath, String filename) {
        File f = null;
        if (filename != null && dirPath != null && !filename.isEmpty() && !dirPath.isEmpty()) {
            f = new File(dirPath, filename + ".txt");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    Log.i("Yay", "Encontrei o dir: " + f.getCanonicalPath());
                } catch (IOException e) {
                    return null;
                }
            } else {
                Log.e("Error", "Already exists the file with that name");
                return null;
            }
            try {
                Log.i("Info", f.getCanonicalPath());
                return f.getCanonicalPath();
            } catch (IOException e) {
                Log.e("Error", e.toString());
            }
        }
        return null; // Já existe um ficheiro com esse nome
    }

    private static File getFile(String filePath) {
        File file = new File(filePath);

        if (filePath != null && !filePath.isEmpty())
            if (file.exists())
                return file;

        return null; // O file que procura não existe ou não foi introduzido nenhum file path
    }

    public static String getFileContent(String filePath) {
        File file = getFile(filePath);

        if (file == null)
            return null;

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            Log.e("read", "Can not read file: " + e.toString());
            return null;
        }

        return text.toString();
    }

    public static boolean setFileContent(String filePath, String content) {
        File file = getFile(filePath);

        if (file == null)
            return false;

        try {
            Writer writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();

            return true;
        } catch (IOException e) {
            Log.e("write", "Can not write file: " + e.toString());
        }
        return false;
    }

    public static String createWorkspace(String userEmail, String wsName) {
        /*
            Creates a directory inside /sdcard/AirDesk/ with the Workspace name
            Note: This approach is useful to have different files with the
                  same name in different workspaces.
         */
        String dirName = userEmail + "/" + wsName;


        File newDir = new File(sdcard0 + "/AirDesk/", dirName);

        if (!newDir.exists()) {
                newDir.mkdirs();
                Log.i("Yay", "Criei o dir: " + wsName );
        } else {
            Log.e("Error", "Já existe o dir: " + wsName );
        }

        try {
            return newDir.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteWorkspace(String dirPath) {
        File ws = new File(dirPath);
        if (ws.isDirectory()) {
            String[] children = ws.list();
            for (int i = 0; i < children.length; i++) {
                if (!new File(ws, children[i]).delete()) {
                    return false;
                }
            }
            return ws.delete();
        }
        return false;
    }

    public static boolean deleteFile(String filePath) {
        File file = getFile(filePath);

        if (file != null) {
            return file.delete();
        }
        return false;
    }

}