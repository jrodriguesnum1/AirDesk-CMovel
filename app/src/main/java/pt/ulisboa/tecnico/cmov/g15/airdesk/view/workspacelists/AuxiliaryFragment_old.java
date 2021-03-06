package pt.ulisboa.tecnico.cmov.g15.airdesk.view.workspacelists;

/**
 * Created by MSC on 06/04/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.R;

public class AuxiliaryFragment_old extends Fragment{

    private AirDesk mAirdesk;
    private Button mPopulateButton;
    private Button mDisconnectNetworkButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.auxiliary_layout, container, false);
        mPopulateButton = (Button) rootView.findViewById(R.id.populateAuxiliaryBtn);
        mDisconnectNetworkButton = (Button) rootView.findViewById(R.id.disconnectAuxiliaryBtn);

        mAirdesk = (AirDesk) getActivity().getApplication();
        mPopulateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPopulateAirDesk(v);
            }
        });
        mDisconnectNetworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDisconnectNetwork(v);
            }
        });
        return rootView;
    }

    public void  onClickPopulateAirDesk(View v) {
        mAirdesk.populate();
        Toast.makeText(getActivity().getApplicationContext(),
                "Workspace created", Toast.LENGTH_SHORT).show();
    }

    public void  onClickDisconnectNetwork(View v) {
        //TODO fazer disconnect
        Toast.makeText(getActivity().getApplicationContext(),
                "Not Implemented", Toast.LENGTH_SHORT).show();
    }


}
