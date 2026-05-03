[Setup]
AppId={{8E1B615D-1E69-4B61-9F60-6E1A7964A801}
AppName=TPVNoteERP
AppVersion=1.0.0
AppPublisher=Jorge Moncada y Carlos Escribano
DefaultDirName={autopf}\TPVNoteERP
DefaultGroupName=TPVNoteERP
DisableProgramGroupPage=yes
OutputDir=Output
OutputBaseFilename=TPVNoteERP-Setup
Compression=lzma
SolidCompression=yes
WizardStyle=modern
ArchitecturesInstallIn64BitMode=x64compatible

[Languages]
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"
[Tasks]
Name: "desktopicon"; Description: "Crear acceso directo en el escritorio"; GroupDescription: "Accesos directos:"
[Files]
Source: "dist\TPVNoteERP.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "dist\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "Informes\*"; DestDir: "{app}\Informes"; Flags: ignoreversion recursesubdirs createallsubdirs
; Si existe una carpeta runtime se puede descomentar para distribuir Java 8 embebido.
;Source: "runtime\*"; DestDir: "{app}\runtime"; Flags: ignoreversion recursesubdirs createallsubdirs
[Icons]
Name: "{group}\TPVNoteERP"; Filename: "{app}\TPVNoteERP.bat"
Name: "{autodesktop}\TPVNoteERP"; Filename: "{app}\TPVNoteERP.bat"; Tasks: desktopicon
[Run]
Filename: "{app}\TPVNoteERP.bat"; Description: "Iniciar TPVNoteERP"; Flags: nowait postinstall skipifsilent
[Code]
procedure CreateLauncher();
var
  LauncherPath: string;
  LauncherContent: string;
begin
  LauncherPath := ExpandConstant('{app}\TPVNoteERP.bat');
  LauncherContent :=
    '@echo off' + #13#10 +
    'set "APP_HOME=%~dp0"' + #13#10 +
    'set "JAVA_EXE=%APP_HOME%runtime\bin\javaw.exe"' + #13#10 +
    'if exist "%JAVA_EXE%" (' + #13#10 +
    '  start "" "%JAVA_EXE%" -jar "%APP_HOME%TPVNoteERP.jar"' + #13#10 +
    ') else (' + #13#10 +
    '  start "" javaw -jar "%APP_HOME%TPVNoteERP.jar"' + #13#10 +
    ')' + #13#10 +
    'exit /b 0' + #13#10;
  SaveStringToFile(LauncherPath, LauncherContent, False);
end;
procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
    CreateLauncher();
end;
