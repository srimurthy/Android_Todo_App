package com.srimurthy.android_todo_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private final int REQUEST_CODE = 200;

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

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TodoActivity.this, EditTaskActivity.class);
                intent.putExtra("task_position",position);
                intent.putExtra("task_text", ((TextView)view).getText());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            int position = data.getExtras().getInt("task_position");
            String originalTaskText = this.todoItemsList.get(position);
            String editedTaskText = data.getExtras().getString("edited_task_text");

            if(originalTaskText.equals(editedTaskText)) {
                //Nothing to update
                return;
            }

            TodoActivity.this.todoItemsList.remove(position);
            TodoActivity.this.todoItemsList.add(position,editedTaskText);
            TodoActivity.this.todoArrayAdapter.notifyDataSetChanged();
            writeItems();
        }
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
