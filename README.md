# Applicazione-web-per-un-servizio-di-ripetizione
progetto di Tecnologie Web <br>
**frontend:** Bootstrap & Vue.Js <br>
**backend:** Java Servlet <br>

# Requisiti

Si sviluppi un’applicazione java web che gestisca prenotazioni di ripetizioni online
essendo dotata sia di interfaccia utente per browser web che per accesso mobile.
  • Si assuma che per ogni corso di cui si offrono ripetizioni ci siano uno più docenti
  alternativi tra cui scegliere con chi fare la ripetizione (per es., lunedi dalle 9.00
  alle 10.00 la lezione di matematica può essere tenuta sia da Mario Rossi che
  da Gianni Verdi).
  
  • Si assuma che ogni docente possa insegnare più di un corso (per es., Mario
  Rossi insegna sia matematica che scienze).
  
  • Si assuma che l’applicazione presenti le ripetizioni disponibili di una settimana
  pre-fissata, dal lunedi al venerdi, al pomeriggio (15.00 – 19.00). In altre parole,
  non è richiesto di gestire un vero calendario ma solo una griglia di slot temporali
  di dimensioni limitate (1h ciascuno). Ogni ripetizione disponibile è associata a
  un orario (dalle ore X alle ore Y del giorno Z) e ai docenti che la potrebbero tenere in quell’orario in quanto non occupati.
  
Seguono i dettagli richiesti per il progetto.
L’applicazione deve gestire tre tipi di ruolo utente: utente ospite (non registrato),
utente registrato (cliente del servizio di ripetizioni) e amministratore. Si assuma di
avere 1 amministratore e 2 clienti pre-registrati.
 Nel dettaglio, l’applicazione deve permettere all’utente di eseguire le seguenti azioni:
 
  • Visualizzare il catalogo delle ripetizioni disponibili, specificando i docenti
  disponibili per ogni ripetizione prenotabile - tutti i ruoli, interfaccia web e
  mobile.
  
  • Prenotare una o più ripetizioni, tra quelle disponibili, scegliendo il corso e il
  docente desiderato – clienti e amministratori, interfaccia web e mobile.
  • Disdire una prenotazione specifica – clienti e amministratori, interfaccia web e
  mobile.
  
  • Segnare una ripetizione come effettuata – ruolo cliente, interfaccia web
  e mobile.
  
  • Visualizzare la propria lista delle prenotazioni. La lista deve includere sia le
  ripetizioni ancora da fruire che lo storico delle ripetizioni prenotate in passato
  (NB: se si rimuove un docente, o un corso, lo storico deve rimanere). Per
  ogni ripetizione in elenco, l’applicazione deve visualizzare lo stato della
  ripetizione (attiva/effettuata/disdetta) – clienti e amministratori, interfaccia
  web e mobile.
  
  • Visualizzare tutte le prenotazioni attive, effettuate e cancellate dei vari clienti -
  solo amministratore, solo interfaccia web.
  
  • Inserire/rimuovere corsi e docenti, e associazioni corso-docente ai fini delle
  ripetizioni - solo amministratore, solo interfaccia web.
  NB: assumete che, quando parte l’applicazione, tutti i docenti siano disponibili in tutti
  gli slot temporali della griglia. Le loro disponibilità verranno cancellate man mano
  che i clienti prenoteranno le loro lezioni. Non sviluppate nessuna interfaccia utente per
  i docenti, essi non devono eseguire nessuna operazione sull’applicazione.
  
  NB: la scelta del dominio applicativo (prenotazioni) non è vincolante e sono ammessi
  altri tipi di dominio purché si rispettino le specifiche funzionali e tecniche descritte in
  questo documento.

## Requisiti tecnici:

• L’applicazione deve essere basata su architettura MVC, con Controller + viste e
Model. Si noti che non deve esserci comunicazione diretta tra viste e model:
ogni tipo di comunicazione tra questi due livelli deve essere mediato dal
controller.

• È obbligatorio gestire le sessioni utente.
• L’applicazione deve salvare in un database relazionale a scelta i seguenti tipi
di informazione:
  o account, password e ruolo degli utenti registrati;
  o titolo dei corsi di cui si offrono le ripetizioni;
  o nome e cognome dei docenti che tengono le ripetizioni;
  o associazioni corso-docente;
  o prenotazioni di ripetizioni.
  
• L’applicazione deve controllare l’inserimento di input utente sia lato client che lato
server per evitare che l’utente inserisca dati parziali o errati nei form (per
esempio, per evitare che l’utente cerchi di collegarsi senza inserire login e
password).

• L’applicazione deve controllare sia lato client che lato server che gli utenti non
eseguano operazioni illecite. Per es., gli utenti non autenticati possono vedere
il catalogo delle ripetizioni disponibili, ma non possono segnare come
effettuate, o disdire, le prenotazioni; solo gli amministratori devono poter
inserire/rimuovere corsi in catalogo; ogni utente (tranne l’amministratore) deve
poter vedere solo le proprie ripetizioni e non quelle altrui.

Requisiti generali dell’interfaccia utente (sia web che mobile):
 L’interfaccia utente deve essere: <br>
    - Comprensibile (trasparenza). Per esempio, a fronte di errori, deve
    segnalare il problema; quando un’operazione viene eseguita con
    successo, deve visualizzare la conferma di esecuzione, a meno che la
    conferma non sia ridondante (in quanto il risultato si vede direttamente
    sull’interfaccia utente). <br>
    - Ragionevolmente efficiente per permettere all’utente di eseguire le
    operazioni con un numero minimo di click e di inserimenti di dati. <br>
    - In caso di errore durante l’inserimento di dati nelle form, l’interfaccia deve
    permettere all’utente di correggere i dati e ripetere l’operazione senza
    perdere i dati precedentemente inseriti (cioè, senza riempire d’accapo
    i moduli online). <br>
    Requisiti specifici per l’interfaccia utente per il web:
    - L’interfaccia web deve essere implementata come Single Page
    Application utilizzando Vue.js e HTML5; Il layout delle pagine
    dell’interfaccia utente deve essere specificato con CSS e deve essere
    fluido.  <br>
    - Il controllo dell’input utente lato client deve essere effettuato utilizzando i
    tag di HTML5 e/o JavaScript.<br>
    - Il backend dell’applicazione deve essere implementato in Java<br>

### TODO: 
done
