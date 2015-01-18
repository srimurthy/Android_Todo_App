package com.srimurthy.android_todo_app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TodoActivity extends ActionBarActivity {

    private List<String> todoItemsList;
    private ArrayAdapter<String> todoArrayAdapter;
    private EditText taskInputTextView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        readItems();
        this.todoArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.todoItemsList);
        this.listView = (ListView)findViewById(R.id.listView);
        this.listView.setAdapter(todoArrayAdapter);
        this.taskInputTextView = (EditText)findViewById(R.id.etNewItem);
        setupListViewListener();
    }

    private void setupListViewListener() {
        this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoActivity.this.todoItemsList.remove(position);
                TodoActivity.this.todoArrayAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            this.todoItemsList = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            this.todoItemsList = new ArrayList<>(10);
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, this.todoItemsList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onItemAdd(View view) {
        String newTask = this.taskInputTextView.getText().toString();
        if("".equals(newTask) == false) {
            this.todoArrayAdapter.add(newTask);
            this.taskInputTextView.setText("");
            writeItems();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo, menu);
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
