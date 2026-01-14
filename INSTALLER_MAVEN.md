# 🔧 Installation de Maven

## ⚠️ Problème : Maven n'est pas reconnu

Si vous voyez l'erreur :
```
mvn: The term 'mvn' is not recognized
```

## ✅ Solution Rapide (Recommandée)

### Option 1 : Script Automatique (PowerShell en Administrateur)

1. **Ouvrez PowerShell en tant qu'administrateur** :
   - Clic droit sur PowerShell > "Exécuter en tant qu'administrateur"

2. **Naviguez vers le projet** :
   ```powershell
   cd C:\Users\user\Desktop\padelService_projet_2
   ```

3. **Exécutez le script d'installation** :
   ```powershell
   .\installer-maven.ps1
   ```

4. **Fermez et rouvrez votre terminal** pour que le PATH soit mis à jour

5. **Vérifiez l'installation** :
   ```powershell
   mvn -version
   ```

---

### Option 2 : Installation Manuelle

1. **Téléchargez Maven** :
   - Allez sur : https://maven.apache.org/download.cgi
   - Téléchargez : `apache-maven-3.9.9-bin.zip`

2. **Extrayez l'archive** :
   - Extrayez dans : `C:\Program Files\Apache\maven`

3. **Ajoutez Maven au PATH système** :
   - Ouvrez "Variables d'environnement" (Windows + R, tapez `sysdm.cpl`)
   - Onglet "Avancé" > "Variables d'environnement"
   - Dans "Variables système", sélectionnez "Path" > "Modifier"
   - Cliquez "Nouveau" et ajoutez : `C:\Program Files\Apache\maven\bin`
   - Cliquez "OK" partout

4. **Fermez et rouvrez votre terminal**

5. **Vérifiez** :
   ```powershell
   mvn -version
   ```

---

### Option 3 : Utiliser Chocolatey (si installé)

```powershell
choco install maven
```

---

## ✅ Après l'Installation

Une fois Maven installé, vous pouvez lancer le projet :

```powershell
cd C:\Users\user\Desktop\padelService_projet_2
demarrer-tout.bat
```

---

## 🔍 Vérification

Vérifiez que tout est installé :

```powershell
java -version    # Doit afficher Java 21 ou supérieur
mvn -version     # Doit afficher Maven 3.9.x
node --version   # Doit afficher Node.js 18.x ou supérieur
docker --version # Doit afficher Docker
```

---

## 📝 Note

Si Maven est installé mais toujours non reconnu :
1. Fermez **complètement** votre terminal PowerShell
2. Rouvrez un nouveau terminal
3. Vérifiez avec `mvn -version`

Le PATH système nécessite un redémarrage du terminal pour être pris en compte.

