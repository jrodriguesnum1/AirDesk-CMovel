package pt.ulisboa.tecnico.cmov.g15.airdesk.network;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.AirDeskFile;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.enums.FileState;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteClientSide;
import pt.ulisboa.tecnico.cmov.g15.airdesk.network.remotes.RemoteCommunicatorI;

/**
 * Created by MSC on 05/04/2015..
 */
public class NetworkServiceClient {
    //TODO when WIFIDirect is implemented, use its handler
    private static Map<String, NetworkServiceServerI> servers = new HashMap<String, NetworkServiceServerI>();

    public static void addNetworkServiceServer(String userEmail, NetworkServiceServerI server) {
        servers.put(userEmail, server);
    }

    public static void addNewElementOffNetwork(RemoteCommunicatorI communicator) {
        Log.e("json", "networkclient.new connection");
        NetworkServiceServerI server = new RemoteClientSide(communicator);
        String serverEmail = server.getEmail();
        servers.put(serverEmail, server);
    }

    private static NetworkServiceServerI getWorkspaceOwnerServer(Workspace workspace) {
        return servers.get(workspace.getOwner().getEmail());
    }

    private static NetworkServiceServerI getUserServer(User user) {
        return servers.get(user.getEmail());
    }

    public static boolean notifyIntention(Workspace workspace, AirDeskFile file, FileState intention, boolean force) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        return fileOwnerServer.notifyIntentionS(workspace.getName(), file.getName(), intention, force);
    }


    public static int getFileVersion(Workspace workspace, AirDeskFile file) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        return fileOwnerServer.getFileVersionS(workspace.getName(), file.getName());
    }


    public static FileState getFileState(Workspace workspace, AirDeskFile file) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        return fileOwnerServer.getFileStateS(workspace.getName(), file.getName());
    }


    public static String getFile(Workspace workspace, String fileName) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        return fileOwnerServer.getFileS(workspace.getName(), fileName);
    }


    public static void sendFile(Workspace workspace, String fileName, String fileContent) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        fileOwnerServer.sendFileS(workspace.getName(), fileName, fileContent);
    }

    //TODO temporary
    /*public static void setAirDesk(AirDesk airDesk) {
        networkServiceServer.setAirDesk(airDesk);
    }*/

    // this method is no longer used. the workspaces are refreshed on the foreign workspace activity
    /*
    public static void removeWorkspace(OwnerWorkspace workspace) {
        //TODO broadcast to accessList
        String email = workspace.getOwner().getEmail();
        networkServiceServer.workspaceRemovedS(workspace);
    }*/

    // this method is no longer used. the workspaces are refreshed on the foreign workspace activity
    /*
    public static void removeUserFromAccessList(OwnerWorkspace ownerWorkspace, User user) {
        networkServiceServer.workspaceRemovedS(ownerWorkspace);
    }
    */

    public static void deleteFile(Workspace workspace, AirDeskFile airDeskFile) {
        NetworkServiceServerI fileOwnerServer = getWorkspaceOwnerServer(workspace);
        fileOwnerServer.deleteFileS(workspace.getName(), airDeskFile.getName());
        Log.e("deleteFile", "file remotly deleted: " + airDeskFile.getName());
    }

    public static Map<String, List<String>> searchWorkspaces(String email, List<String> tags) {
        Map<String,List<String>> searchResult = new HashMap<String,List<String>>();
        for(String foreignEmail : servers.keySet()) {
            NetworkServiceServerI workspaceOwnerServer = servers.get(foreignEmail);
            List<String> workspaceNames = workspaceOwnerServer.searchWorkspacesS(email, tags);
            searchResult.put(foreignEmail, workspaceNames);
        }
        return searchResult;
    }

    public static List<String> searchFiles(String userEmail, String workspaceName) {
        NetworkServiceServerI fileOwnerServer = servers.get(userEmail);
        return fileOwnerServer.getFileList(workspaceName);
    }

    public static long getWorkspaceQuota(String ownerEmail, String workspaceName) {
        NetworkServiceServerI workspaceOwnerServer = servers.get(ownerEmail);
        return workspaceOwnerServer.getWorkspaceQuotaS(ownerEmail, workspaceName);
    }

    public static void notifyWorkspaceDeleted(String ownerEmail, String workspaceName) {
        for(NetworkServiceServerI server : servers.values()) {
            server.workspaceRemovedS(ownerEmail, workspaceName);
        }
    }

    public static void notifyFileDeleted(String ownerEmail, String workspaceName, String fileName) {
        for(Map.Entry<String, NetworkServiceServerI> entry: servers.entrySet()) {
            Log.e("deleteFile", "NSC.notify " + entry.getKey() + " to delete: " + fileName);
            entry.getValue().fileRemovedS(ownerEmail, workspaceName, fileName);
        }
    }
}
