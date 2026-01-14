# Script d'installation de Maven pour Windows
# Exécuter en tant qu'administrateur : clic droit > Exécuter en tant qu'administrateur

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Installation de Maven" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Vérifier si Maven est déjà installé
$mavenInstalled = Get-Command mvn -ErrorAction SilentlyContinue
if ($mavenInstalled) {
    Write-Host "Maven est deja installe :" -ForegroundColor Green
    mvn -version
    Write-Host ""
    Write-Host "Maven est deja dans le PATH. Aucune installation necessaire." -ForegroundColor Yellow
    pause
    exit 0
}

Write-Host "Maven n'est pas installe. Installation en cours..." -ForegroundColor Yellow
Write-Host ""

# Créer le dossier d'installation
$installDir = "C:\Program Files\Apache\maven"
$mavenVersion = "3.9.9"
$mavenUrl = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$zipFile = "$env:TEMP\apache-maven-$mavenVersion-bin.zip"
$extractDir = "$env:TEMP\apache-maven-$mavenVersion"

try {
    # Télécharger Maven
    Write-Host "Telechargement de Maven $mavenVersion..." -ForegroundColor Cyan
    Invoke-WebRequest -Uri $mavenUrl -OutFile $zipFile -UseBasicParsing
    
    # Extraire l'archive
    Write-Host "Extraction de l'archive..." -ForegroundColor Cyan
    Expand-Archive -Path $zipFile -DestinationPath $env:TEMP -Force
    
    # Créer le dossier d'installation
    if (-not (Test-Path $installDir)) {
        New-Item -ItemType Directory -Path $installDir -Force | Out-Null
    }
    
    # Copier les fichiers
    Write-Host "Installation dans $installDir..." -ForegroundColor Cyan
    Copy-Item "$extractDir\apache-maven-$mavenVersion\*" -Destination $installDir -Recurse -Force
    
    # Ajouter au PATH système
    Write-Host "Ajout de Maven au PATH systeme..." -ForegroundColor Cyan
    $mavenBin = "$installDir\bin"
    $currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
    
    if ($currentPath -notlike "*$mavenBin*") {
        [Environment]::SetEnvironmentVariable("Path", "$currentPath;$mavenBin", "Machine")
        Write-Host "Maven ajoute au PATH systeme." -ForegroundColor Green
    }
    
    # Nettoyer
    Remove-Item $zipFile -Force -ErrorAction SilentlyContinue
    Remove-Item $extractDir -Recurse -Force -ErrorAction SilentlyContinue
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "  Maven installe avec succes !" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "IMPORTANT : Fermez et rouvrez votre terminal PowerShell" -ForegroundColor Yellow
    Write-Host "pour que les changements du PATH prennent effet." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Ensuite, verifiez avec : mvn -version" -ForegroundColor Cyan
    Write-Host ""
    
} catch {
    Write-Host ""
    Write-Host "ERREUR lors de l'installation :" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host ""
    Write-Host "Installation manuelle :" -ForegroundColor Yellow
    Write-Host "1. Telechargez Maven depuis : https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host "2. Extrayez dans C:\Program Files\Apache\maven" -ForegroundColor Yellow
    Write-Host "3. Ajoutez C:\Program Files\Apache\maven\bin au PATH systeme" -ForegroundColor Yellow
    Write-Host ""
}

pause

