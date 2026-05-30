[Setup]
AppId={{8E1B615D-1E69-4B61-9F60-6E1A7964A801}}
AppName=NoteERP
AppVersion=1.0
AppPublisher=Jorge Moncada y Carlos Escribano
DefaultDirName={autopf}\NoteERP
DefaultGroupName=NoteERP
DisableProgramGroupPage=yes
OutputDir=output
OutputBaseFilename=NoteERPSetup
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"

[Tasks]
Name: "desktopicon"; Description: "Crear acceso directo en el escritorio"; GroupDescription: "Accesos directos:"

[Files]
Source: "dist\TPVNoteERP.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "dist\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "src\Informes\*"; DestDir: "{app}\Informes"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "mariadb\*"; DestDir: "{app}\mariadb"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\NoteERP"; Filename: "{app}\NoteERP.bat"
Name: "{autodesktop}\NoteERP"; Filename: "{app}\NoteERP.bat"; Tasks: desktopicon

[Run]
Filename: "{app}\NoteERP.bat"; Description: "Iniciar NoteERP"; Flags: nowait postinstall skipifsilent

[Code]
procedure CreateLauncher();
var
  LauncherPath: string;
  LauncherContent: string;
begin
  LauncherPath := ExpandConstant('{app}\NoteERP.bat');
  LauncherContent :=
    '@echo off' + #13#10 +
    'cd /d "%~dp0"' + #13#10 +
    'tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL' + #13#10 +
    'if "%ERRORLEVEL%"=="1" (' + #13#10 +
    '    start "" "%~dp0mariadb\bin\mysqld.exe" --defaults-file="%~dp0mariadb\my.ini" --datadir="%~dp0mariadb\data"' + #13#10 +
    '    timeout /t 3 /nobreak >nul' + #13#10 +
    ')' + #13#10 +
    '"%~dp0jre\bin\javaw.exe" -jar "%~dp0TPVNoteERP.jar"' + #13#10 +
    'exit /b 0';
  SaveStringToFile(LauncherPath, LauncherContent, False);
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
  begin
    CreateLauncher();
  end;
end;