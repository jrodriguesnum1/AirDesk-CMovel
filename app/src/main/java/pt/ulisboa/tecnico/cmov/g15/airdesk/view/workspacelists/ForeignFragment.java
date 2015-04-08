package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;

/**
 * Created by MSC on 06/04/2015.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.FileListActivity;
import pt.ulisboa.tecnico.cmov.g15.airdesk.view.utils.ListAdapter;

public class ForeignFragment extends Fragment {

    private AirDesk mAirDesk;
    private ListAdapter<ForeignWorkspace> mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.foreign_layout, container, false);
        mAirDesk = (AirDesk) getActivity().getApplication();
        ListView listView = (ListView) rootView.findViewById(R.id.foreign_workspaces_list);

        List<ForeignWorkspace> elements = mAirDesk.getForeignWorkspaces();

        mListAdapter = new ListAdapter<ForeignWorkspace>(getActivity(), R.layout.foreign_workspace_item, elements) {
            @Override
            public void initItemView(final ForeignWorkspace workspace, View view, final int position) {
                final String workspaceName = workspace.getName();
                TextView textView = (TextView) view.findViewById(R.id.workspace_name);
                textView.setText(workspaceName);

                Button removeForeignWorkspaceButton = (Button) view.findViewById(R.id.remove_workspace_button);
                removeForeignWorkspaceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickRemoveForeignWorkspace(workspace, v, position);
                    }
                });
            }
        };

        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickListWorkspaceFiles(mListAdapter.getItem(position), view);
            }
        });

        Button editTagsButton = (Button) rootView.findViewById(R.id.edit_tags_button);
        editTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEditTags(v);
            }
        });

        return rootView;
    }

    public void onClickRemoveForeignWorkspace(ForeignWorkspace workspace, View v, int position) {
        final String workspaceName = workspace.getName();
        final Integer workspaceId = workspace.getId();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder
                .setMessage("Are you sure you want to delete the workspace '" + workspaceName + "'?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "TO DO: delete workspace", Toast.LENGTH_SHORT).show();
                        mAirDesk.deleteForeignWorkspace(workspaceId);
                        mListAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog deleteFileDialog = alertDialogBuilder.create();
        deleteFileDialog.show();
    }

    public void onClickListWorkspaceFiles(ForeignWorkspace workspace, View v) {
        Intent intent = new Intent(getActivity(), FileListActivity.class);
        intent.putExtra(FileListActivity.EXTRA_WORKSPACE_ID, workspace.getId());
        startActivity(intent);
        //getActivity().finish();
    }

    public void onClickEditTags(View v) {
        User user = mAirDesk.getUser();
        final EditText input = new EditText(v.getContext());
        input.setHint("Tags separated by ;");
        String tags = "";
        for(int i=0; i<user.getUserTags().size(); i++){
            tags+=user.getUserTags().get(i);
            if( (i+1) != user.getUserTags().size()) tags+=";";
        }
        input.setText(tags);
        AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                .setTitle("Edit Tags")
                .setMessage("Tags: ")
                .setView(input)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int which) {
                        String new_tags = input.getText().toString();
                        if (!new_tags.equals("")) {
                            String[] split = new_tags.split(";");
                            List<String> tagList = mAirDesk.getUser().getUserTags();
                            tagList.clear();
                            for(String s: split) tagList.add(s.trim());
                            mAirDesk.getUser().setUserTags(tagList);
                            mListAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity().getApplicationContext(), "Tags have been updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Invalid tags", Toast.LENGTH_SHORT).show();
                        }
                        di.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int which) {
                        di.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}
