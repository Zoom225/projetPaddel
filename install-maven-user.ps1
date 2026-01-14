# Script d'installation de Maven pour l'utilisateur courant (sans Admin)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Installation Locale de Maven" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Définir les variables
$installDir = "$env:USERPROFILE\.maven"
$mavenVersion = "3.9.12"
$mavenUrl = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$zipFile = "$env:TEMP\apache-maven-$mavenVersion-bin.zip"
$extractDir = "$env:TEMP\apache-maven-$mavenVersion"

try {
    # 1. Vérifier si déjà installé
    if (Test-Path "$installDir\apache-maven-$mavenVersion\bin\mvn.cmd") {
        Write-Host "Maven semble deja present dans $installDir" -ForegroundColor Yellow
    }
    else {
        # 2. Télécharger
        Write-Host "Telechargement de Maven $mavenVersion..." -ForegroundColor Cyan
        Invoke-WebRequest -Uri $mavenUrl -OutFile $zipFile -UseBasicParsing

        # 3. Extraire
        Write-Host "Extraction..." -ForegroundColor Cyan
        Expand-Archive -Path $zipFile -DestinationPath $env:TEMP -Force

        # 4. Déplacer vers le dossier utilisateur
        if (-not (Test-Path $installDir)) {
            New-Item -ItemType Directory -Path $installDir -Force | Out-Null
        }
        
        # Nettoyer l'ancienne version si elle existe pour éviter les conflits
        if (Test-Path "$installDir\apache-maven-$mavenVersion") {
            Remove-Item "$installDir\apache-maven-$mavenVersion" -Recurse -Force
        }

        Copy-Item "$env:TEMP\apache-maven-$mavenVersion" -Destination $installDir -Recurse -Force
        Write-Host "Fichiers installes dans $installDir" -ForegroundColor Green
    }

    # 5. Ajouter au PATH Utilisateur
    $mavenBin = "$installDir\apache-maven-$mavenVersion\bin"
    $currentPath = [Environment]::GetEnvironmentVariable("Path", "User")

    if ($currentPath -notlike "*$mavenBin*") {
        Write-Host "Ajout de Maven au PATH utilisateur..." -ForegroundColor Cyan
        [Environment]::SetEnvironmentVariable("Path", "$currentPath;$mavenBin", "User")
        Write-Host "PATH mis a jour." -ForegroundColor Green
    }
    else {
        Write-Host "Maven est deja dans le PATH utilisateur." -ForegroundColor Yellow
    }

    # Nettoyage
    Remove-Item $zipFile -Force -ErrorAction SilentlyContinue
    Remove-Item $extractDir -Recurse -Force -ErrorAction SilentlyContinue

    Write-Host ""
    Write-Host "Installation terminee avec succes !" -ForegroundColor Green
    Write-Host "Veuillez REDEMARRER votre terminal pour prendre en compte les changements." -ForegroundColor Yellow

}
catch {
    Write-Host "Erreur lors de l'installation : $_" -ForegroundColor Red
}
