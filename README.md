# SleepWell

O SleepWell é um aplicativo Android desenvolvido para auxiliar os usuários a monitorar e melhorar a qualidade do sono. O app oferece diversas funcionalidades, como diário de sonhos, despertador inteligente, monitoramento de ciclos do sono e dicas para melhorar o descanso. A interface é moderna, com tema escuro, e foi construída utilizando o Jetpack Compose.

---

## Funcionalidades

- **Autenticação de Usuário:**  
  Permite o cadastro e login do usuário utilizando e-mail e senha, com dados armazenados no Firebase Firestore.

- **Diário de Sonhos:**  
  Registre seus sonhos em um diário interativo, onde você pode adicionar, visualizar e excluir registros.

- **Despertador Inteligente:**  
  Configure alarmes baseados em ciclos de sono para acordar no momento ideal.

- **Dicas de Sono:**  
  Exiba dicas e sugestões para melhorar os hábitos de sono, buscadas do Firestore.

- **Monitoramento de Ciclos:**  
  Analisa os ciclos de sono e apresenta sugestões de horários para acordar.

- **Interface Moderna e Responsiva:**  
  Desenvolvida com Jetpack Compose, utilizando um tema escuro consistente em todas as telas.

---

## Arquitetura do Aplicativo

O aplicativo segue uma arquitetura MVC (Model-View-Controller):

- **Model:**  
  Define as entidades do aplicativo (por exemplo, `Usuario`, `DiarioDeSonho`, `Dica`) e gerencia a sessão do usuário através do `SessionManager`.

- **View:**  
  Implementa a interface do usuário utilizando o Jetpack Compose. As telas (como Login, Cadastro, Diário de Sonhos, Despertador, Dicas, Splash, etc.) são compostas com funções composables.

- **Controller/Repository:**  
  Contém a lógica de negócio e a comunicação com o Firebase Firestore. Os controllers (como `LoginController`, `SignUpController`, `DiarioDeSonhoController` e `DicaController`) gerenciam operações de autenticação, cadastro e manipulação dos dados.

---

## Tecnologias Utilizadas

- **Kotlin:** Linguagem de programação principal.
- **Jetpack Compose:** Framework para construção de interfaces declarativas.
- **Firebase Firestore:** Banco de dados na nuvem para armazenamento e sincronização dos dados.
- **Serviços do Android:** Utilizados para gerenciar alarmes (ex.: `AlarmReceiver` e `AlarmService`).

---

## Estrutura do Projeto

```
SleepWell/
├── app/
│   └── src/
│       └── main/
│           ├── java/br/edu/ifpb/sleepwell/
│           │   ├── alarm/           // AlarmReceiver, AlarmService
│           │   ├── controller/      // LoginController, SignUpController, DiarioDeSonhoController, DicaController
│           │   ├── model/
│           │   │   ├── data/repository/  // UsuarioRepository, DicaRepository, etc.
│           │   │   └── entity/      // Usuario, DiarioDeSonho, Dica
│           │   ├── utils/           // DataMaskVisualTransformation, formatDate, etc.
│           │   └── view/screens/    // LoginScreen, SignUpScreen, AddDreamScreen, DreamDiaryScreen, AlarmScreen, TipsScreen, SplashScreen, BottomAppBar, etc.
│           └── res/
│               ├── drawable/      // Imagens (ex.: wave_background.png)
│               ├── raw/           // Arquivos de som para alarme
│               └── values/        // Definições de tema e estilos
└── README.md                     // Documentação do projeto (este arquivo)
```

---

## Configuração e Instalação

1. **Clone o Repositório:**

   ```bash
   git clone https://github.com/seuusuario/SleepWell.git
   ```

2. **Abra no Android Studio:**
   - Abra o projeto no Android Studio.
   - Certifique-se de que o Android Studio está atualizado para suportar o Jetpack Compose.

3. **Configurar o Firebase:**
   - Siga o [Guia de Configuração do Firebase para Android](https://firebase.google.com/docs/android/setup) para adicionar o arquivo `google-services.json` na pasta `app`.
   - Habilite o Firestore e outros serviços necessários no console do Firebase.

4. **Build e Execução:**
   - Faça a sincronização do projeto com o Gradle.
   - Construa e execute o app em um dispositivo Android ou emulador (minSdkVersion 24).

---

## Uso do Aplicativo

- **Login e Cadastro:**  
  Novos usuários podem se cadastrar, enquanto usuários já cadastrados podem fazer login. O usuário autenticado é gerenciado pelo `SessionManager`.

- **Diário de Sonhos:**  
  Registre seus sonhos e visualize os registros salvos no Firestore.

- **Despertador:**  
  Configure alarmes baseados nos ciclos de sono e teste o alarme com funções para cancelamento e parada.

- **Dicas de Sono:**  
  Exiba uma lista de dicas para melhorar a qualidade do sono, buscadas diretamente do Firestore.

---

## Melhorias Futuras

- **Validações Avançadas:**  
  Implementar regras de validação mais robustas para e-mail e senha.

- **Aprimoramento da UI/UX:**  
  Refatorar animações e transições, aprimorando a experiência do usuário.

- **Novas Funcionalidades:**  
  Expandir funcionalidades, como estatísticas personalizadas e recomendações de sono.

---

## Licença

Este projeto está licenciado sob o IFPB.

---

## Agradecimentos

- **Firebase:** Por fornecer uma solução robusta de banco de dados na nuvem.
- **Jetpack Compose:** Por facilitar o desenvolvimento de interfaces modernas e declarativas.
- **Material Design:** Por inspirar o design e a aparência do aplicativo.
```
