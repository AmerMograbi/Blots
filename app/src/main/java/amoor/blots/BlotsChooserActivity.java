package amoor.blots;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import amoor.blots.blotsServices.Utils;
import amoor.blots.blotsServices.BlotsCreatorService;

public class BlotsChooserActivity extends AppCompatActivity {

    public final int OVERLAY_PERMISSION_REQ_CODE_BLOT = 1111;
    private GridView gvBlots;
    private int lastBlotPosChosen;
    private String currShape = null;
    private LinearLayout llBlotChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blots_chooser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        llBlotChooser = (LinearLayout) findViewById(R.id.ll_blot_chooser);
        gvBlots = (GridView) findViewById(R.id.gv_blots_chooser);
        gvBlots.setAdapter(new BlotsChooserAdapter(this));
        Utils.setFirstTimeCreatingBlot(getApplicationContext());

        gvBlots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvBlotDesc = (TextView) view.findViewById(R.id.tv_blot_desc);
                currShape = tvBlotDesc.getText().toString();

                if(BlotsCreatorService.blotCreated && lastBlotPosChosen != i){
                    BlotsCreatorService.changeBlotShape(BlotsChooserActivity.this, currShape, "black");
                    lastBlotPosChosen = i;
                    return;
                }else if (BlotsCreatorService.blotCreated){
                    removeBlot();
                    return;
                }

                if(Utils.canDrawOverlays(BlotsChooserActivity.this)){
                    createBlot();
                    if(Utils.firstTimeCreatingBlot){
                        DisplayOnCreateBlotDialog();
                        Utils.firstTimeCreatingBlot = false;
                    }
                }else{
                    DisplayRequestDialog();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(BlotsChooserActivity.this, InfoActivity.class);
                startActivity(myIntent);
            }
        });


    }

    private void DisplayRequestDialog() {
        final AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setMessage(R.string.request_permission_dialog_text);
        dlg.setPositiveButton(R.string.go_to_settings_dialog_button,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openOverlaySettings(OVERLAY_PERMISSION_REQ_CODE_BLOT);
                    }
                });
        dlg.setNegativeButton(R.string.cancel_dialog_button, new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dlg.show();

    }

    private void DisplayOnCreateBlotDialog() {
        final AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setMessage(R.string.blot_on_create);
        dlg.setPositiveButton(R.string.ok_dialog_button,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        dlg.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void openOverlaySettings(int requestCode) {
        Intent mngOverlayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        mngOverlayIntent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(mngOverlayIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OVERLAY_PERMISSION_REQ_CODE_BLOT) {
            //after the user returned from the settings we check if he really enabled the option
            if (Utils.canDrawOverlays(this)) {
                createBlot();
                DisplayOnCreateBlotDialog();
            }
        }

    }


    void createBlot(){
        BlotsCreatorService.blotCreated = true;
        Intent intent = new Intent(BlotsChooserActivity.this, BlotsCreatorService.class);
        intent.putExtra("shape",currShape);
        startService(intent);
        Utils.storeFirstTimeCreatingBlot(getApplicationContext());

    }

    void removeBlot(){
        BlotsCreatorService.blotCreated = false;
        stopService(new Intent(BlotsChooserActivity.this, BlotsCreatorService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blots_chooser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
