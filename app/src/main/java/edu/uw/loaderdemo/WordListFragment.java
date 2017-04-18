package edu.uw.loaderdemo;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple Fragment to display a list of words.
 */
public class WordListFragment extends Fragment implements LoaderManager.LoaderCallbacks {

    private static final String TAG = "WordList";

    private SimpleCursorAdapter adapter;

    public WordListFragment() {
        // Required empty public constructor
    }

    //A factory method to create a new fragment with some arguments
    public static WordListFragment newInstance() {
        WordListFragment fragment = new WordListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word_list, container, false);

        //URI

        // Want to be able to do something like:
        //Log.v(TAG, UserDictionary.Words.CONTENT_URI);

        // Q: Okay, but how to I get access to the database?
        // A: The ContentResolver

        // get the column names from the UserDictionary (the table)
//        String[] projection = { UserDictionary.Words.WORD, UserDictionary.Words._ID };
//
//        // get data from the database with .query()
//        // query returns a Cursor, which is basically a list of results that we can iterate through
//       Cursor cursor = getActivity().getContentResolver().query(
//                UserDictionary.Words.CONTENT_URI,
//                projection,
//                null, null, // our WHERE selectors.  We don't care right now, we just want all the words
//                null // our OrderBy selectors.  Again, we don't care right now.
//        );;
//




//        cursor.moveToFirst(); // move pointer to first element
//        Log.v(TAG, cursor.getString(0)); // get me the word

        //model
        String[] data = {"Dog","Cat","Android","Inconceivable"};

        //controller
        AdapterView listView = (AdapterView)rootView.findViewById(R.id.wordListView);

        int[] views = { };
        adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item_layout,
                null, // null okay
                new String[] {UserDictionary.Words.WORD}, // take first item from this array
                new int[] {R.id.txtListItem}, // and put it into first item of this array
                0
        );


//        For an ArrayAdapter<String>.  We changed adapter to be a SimpleCursorAdapter instance variable
//        adapter = new ArrayAdapter<String>(
//                getActivity(), R.layout.list_item_layout, R.id.txtListItem, data);


        listView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        //handle button input
        final TextView inputText = (TextView)rootView.findViewById(R.id.txtAddWord);
        Button addButton = (Button)rootView.findViewById(R.id.btnAddWord);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputWord = inputText.getText().toString();
                Log.v(TAG, "To add: "+inputWord);

                ContentValues values = new ContentValues();
                values.put(UserDictionary.Words.WORD, inputWord);
                values.put(UserDictionary.Words.APP_ID, "edu.uw.loaderdemo");
                values.put(UserDictionary.Words.LOCALE, "en_US");
                values.put(UserDictionary.Words.FREQUENCY, 100);

                getActivity().getContentResolver().insert(UserDictionary.Words.CONTENT_URI, values);
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { UserDictionary.Words.WORD, UserDictionary.Words._ID };

        CursorLoader loader = new CursorLoader(
                getActivity(),
                UserDictionary.Words.CONTENT_URI,
                projection,
                null, null, null
        );
        return loader;
    }

    // called when we finish querying the database
    @Override
    public void onLoadFinished(Loader loader, Object data) {

        adapter.swapCursor((Cursor)data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }
}
