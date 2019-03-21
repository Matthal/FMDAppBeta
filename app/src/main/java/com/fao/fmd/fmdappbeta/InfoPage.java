package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);

        Bundle bundle = getIntent().getExtras();
        String tag = bundle.getString("tag");

        TextView title = findViewById(R.id.info);
        TextView light = findViewById(R.id.light);
        TextView dark = findViewById(R.id.dark);
        TextView info = findViewById(R.id.info_text);

        String text;

        if(tag.equals("main")){
            title.setText("OPENING SCREEN");

            text = "<b>Welcome to the EuFMD Disease Outbreak Investigation App!</b><br>" +
                    "This app is designed to train the user age lesions from cases of Foot-and-mouth disease and create a farm timeline with tracings to help identify the most likely source of infection on the farm, and the likely locations virus may have spread. The lesion ageing in this app is designed only for oral lesions of cattle.<br>" +
                    "If there are any queries, please email eufmd@fao.org.<br><br>" +
                    "<b>Add a farm</b>: Select this option to create a new farm and to enter data on the animals affected<br><br>" +
                    "<b>My Farms</b>: Selection this option to view previous farms that have been entered<br><br>" +
                    "<b>Biosecurity</b>: Select this option to review biosecurity procedures when investigating outbreaks of foot-and-mouth disease<br><br>" +
                    "<b>Diagnostics</b>: Select this option to review diagnostic options when taking samples to confirm the presence of foot-and-mouth disease";
            info.setText(Html.fromHtml(text));

        }

        if(tag.equals("farm")){
            title.setText("FARM INFORMATION");

            text = "<b>Farm information</b><br><br>" +
                    "On this screen, you should enter:<br><br>" +
                    "• the name of the investigator <br>" +
                    "• the name of the person being interviewed<br>" +
                    "• the position of this person on the farm<br>" +
                    "• the name of the farm <br>" +
                    "• the address of the farm (e.g. the road name, village, town)<br>" +
                    "• the country the farm is located<br><br>" +
                    "In order to record the GPS co-ordinates of the location, tap the button labelled “GPS”.";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.30f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.70f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        if(tag.equals("animal")){
            title.setText("ANIMAL INFORMATION");

            text = "Use this screen to enter basic information about the individual animal being examined. We suggest you provide the following information:<br><br>" +
                    "• ID number or name of the animal<br>" +
                    "• The name or ID number of the group the animal is in (e.g. Calves 1, dry cows etc)<br>" +
                    "• Age (select the number of years and months)<br>" +
                    "• Date of onset of the clinical signs as indicated by the person being interviewed<br>" +
                    "• Vaccination status (has the animal received a FMD vaccine in the previous 6 moths)";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.45f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.55f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        if(tag.equals("lesion")){
            title.setText("LESION DESCRIPTION");

            text = "Use the options on this screen to help describe the lesion you are observing and the app will suggest a lesion age. Select the button labelled “Gallery” to observe images to help you decide.<br><br>" +
                    "• Vesicle – these are fluid filled structures located on the tongue, dental pad or lips<br>" +
                    "• Fibrin – this is a yellow coloured substance that is produced by the body as part of the healing process<br>" +
                    "• Colour of base of lesion – look in the centre of the lesion and notice the colour which is usually either bright red, pink, or white/grey<br>" +
                    "• Edges of lesion – are they sharp or smooth?<br><br>" +
                    "On this screen you can also choose to take a photograph and label the exact lesion you are ageing. Select the camera icon to do this.";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.60f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.40f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        if(tag.equals("complete")){
            title.setText("LESION AGEING CALCULATOR");

            text = "This screen indicates the likely age of the lesions based on the answers you gave in the lesion description. <br><br>" +
                    "Select “<b>See examples</b>” to see other examples of lesions of this age.<br><br>" +
                    "Select “<b>Add another lesion</b>” if you have seen another lesion in the mouth of this animal.<br><br>" +
                    "Select “<b>Diagnostic Options</b>” to see how you can confirm FMD in an animal that has a lesion of this age.<br><br>" +
                    "Select “<b>Assumptions</b>” to see what assumptions are used to calculate the lesion ages. Remember this is designed only for oral lesions of cattle.<br><br>" +
                    "Select “<b>Add to farm timeline</b>” to use the lesion age to indicate the likely source of virus to the farm and likely locations if may have spread. You must enter the lesion ages of all animals present on the farm to tell you this. The oldest lesion is key for creating these farm-level transmission timelines.";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.75f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.25f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        if(tag.equals("data")){
            title.setText("ANIMAL DATA COLLECTION");

            text = "Select “<b>Add another animal</b>” to add more animals from this farm. All animals need to be examined and lesions aged in order to get a complete transmission timeline.<br><br>" +
                    "Select “Add tracings” to indicate possible connections with this farm that may be the source of virus, or a location the virus may have spread.<br><br>" +
                    "Select “View farm timeline” to see the dates of likely virus introduction and spread, annotated with the tracings you have entered.<br><br>";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.90f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.10f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        if(tag.equals("tracing")){
            title.setText("TRACING REPORT");

            text = "Here you can enter all the tracings for the farm, to indicate the likely places virus may have come from, and where it may have spread.<br><br>" +
                    "The types of tracings you can enter are:<br><br>" +
                    "• <b>Animal Movements</b> (of FMD susceptible animals – cattle, sheep, goats, pigs etc)<br>" +
                    "   o Enter the species moved<br>" +
                    "• <b>Products</b> (in particular milk and meat)<br>" +
                    "   o Enter the type of product<br>" +
                    "• <b>People</b> (who can carry the virus on their feet, clothing or equipment)<br>" +
                    "   o Enter the category of person and if they had direct contact with the livestock on the farm.<br>" +
                    "• <b>Vehicles</b> (which can pick up virus onto their wheels of other parts)<br>" +
                    "   o Enter the type of vehicle and if it had direct contact with the livestock on the farm.<br>" +
                    "Select the type of tracing you want to add. Select “<b>Enter date</b>” to enter the date of the tracing. On the calendar that pops up you can enter multiple dates if the tracing occurred multiple times.<br><br>" +
                    "In the <b>Notes</b> sections you can add more information for each tracing (e.g. the number of animals moved, location moved to/from etc)<br><br>" +
                    "Select “<b>Add</b>” to move this tracing information to the farm timeline.";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.00f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.00f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        if(tag.equals("timeline")){
            title.setText("FARM TIMELINE");

            text = "This screen indicates the transmission timeline for the farm based on all the data that have been entered on lesion ageing. The red areas indicate the <b>high risk</b> period. The yellow area indicates a relatively <b>lower risk</b>. The tracings are also indicated.<br><br>" +
                    "The accuracy of the farm timeline depends on the information being entered by the user.  All affected animals should be entered to give the most accurate timeline.";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.00f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.00f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        if(tag.equals("list")){
            title.setText("FARMS LIST");

            text = "On this page you can view all previous farms that have been entered on this phone. Touch “Select a farm” to see a list of farms entered (by name of farm). Select a farm to edit the information, add further animals, or view the farm timeline. ";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.30f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.70f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        if(tag.equals("overview")){
            title.setText("DIAGNOSTIC OVERVIEW");

            text = "This page gives an overview of the diagnostics available for FMD virus.<br><br>" +
                    "The top section describes the tests that can be used to detect FMD virus in different sample types.<br><br>" +
                    "The lower section describes considerations for testing structural and non-structural antibodies in serum.";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.00f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.00f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        if(tag.equals("options")){
            title.setText("DIAGNOSTIC OPTIONS");

            text = "This page contains information for samples and tests for FMD virus based on the predicted lesion age.<br><br>" +
                    "You can also find some information on the use of structural and non-structural protein antibody tests.";
            info.setText(Html.fromHtml(text));

            LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.75f
            );
            LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.25f
            );
            light.setLayoutParams(paramLight);
            dark.setLayoutParams(paramDark);
        }

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
    }
}
