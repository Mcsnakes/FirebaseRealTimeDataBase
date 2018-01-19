package sp.fr.firebaserealtimedatabase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sp.fr.firebaserealtimedatabase.model.Author;
import sp.fr.firebaserealtimedatabase.model.Book;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference bookRef;
    private List<Book> bookList;
    private ListView bookListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instance du moteur de base de données
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Référence à la clef Articles enfant de la base de données
        //(table ou collection Articles)
        bookRef = firebaseDatabase.getReference().child("Books");
        bookListView = findViewById(R.id.bookListeView);

        bookList = new ArrayList<>();

        //Récupération des données avec abonnement aux modifications ultérieurs
        bookRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Réinitialisation de la liste des livres
                        bookList.clear();

                        //Boucle sur l'ensemble des noeuds
                        for(DataSnapshot bookSnapshot : dataSnapshot.getChildren() ) {

                            //Création d'une instance de book et hydratation avec les données du snapshot
                            Book book = bookSnapshot.getValue(Book.class);

                            //ajout du livre à la liste
                            bookList.add(book);
                        }
                        Log.d("Main", "Fin de récupération des données");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        Log.d("Main", "Fin de on Create");
        //addBooks();

    }

    private void addBooks() {

        //Création d'un modèle
        String bookTitle = bookRef.push().getKey();
        Book book = new Book();
        book.setTitle(bookTitle).setTitle("Angular pour les nuls");
        book.setAuthor(new Author("seb", "Piwowarski", "Pologne"));
        book.setPrice(25d);
        //Persistence
        bookRef.child(bookTitle).setValue(book);

        //Création d'un modèle
        bookTitle = bookRef.push().getKey();
        book = new Book();
        book.setTitle(bookTitle).setTitle("Android pour les nuls");
        book.setAuthor(new Author("Antoine", "Piwowarski", "Pologne"));
        book.setPrice(25d);
        //Persistence
        bookRef.child(bookTitle).setValue(book);

        //Création d'un modèle
        bookTitle = bookRef.push().getKey();
        book = new Book();
        book.setTitle(bookTitle).setTitle("PHP pour les nuls");
        book.setAuthor(new Author("Jessica", "Gille", "France"));
        book.setPrice(25d);
        //Persistence
        bookRef.child(bookTitle).setValue(book);

        //Création d'un modèle
        bookTitle = bookRef.push().getKey();
        book = new Book();
        book.setTitle(bookTitle).setTitle("JavaScript pour les nuls");
        book.setAuthor(new Author("Jessica", "Gille", "France"));
        book.setPrice(25d);
        //Persistence
        bookRef.child(bookTitle).setValue(book);

    }

    private class BookArrayAdapter extends ArrayAdapter<Book> {

        private Activity context;
        private int ressource;
        private List<Book> data;

        public BookArrayAdapter(@NonNull Activity context, int resource, List<Book> data) {
            super(context, resource, data);

            this.context = context;
            this.data = data;
            this.ressource = resource;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view = context.getLayoutInflater().inflate(this.ressource, parent, false);

            Book currentBook = bookList.get(position);

            return view;
        }
    }

}
