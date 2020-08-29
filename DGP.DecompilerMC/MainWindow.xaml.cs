using DGP.DecompilerMC.Model;
using DGP.DecompilerMC.Service;
using ModernWpf.Controls;
using SourceChord.FluentWPF;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Runtime.CompilerServices;
using System.Windows;

namespace DGP.DecompilerMC
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : AcrylicWindow, INotifyPropertyChanged
    {
        private Minecraft Minecraft = null;
        public static MainWindow Current { get; private set; } = null;
        public MainWindow()
        {
            DataContext = this;
            InitializeComponent();
            Current = this;
            ModernWpf.ThemeManager.Current.ApplicationTheme = ModernWpf.ApplicationTheme.Light;
        }

        private async void Window_Loaded(object sender, RoutedEventArgs e)
        {
            //todo handle web ex
            try
            {
                Minecraft = await VersionsService.GetMinecraftAsync();
            }
            catch(WebException)
            {
                Minecraft = new Minecraft();
            }
            Versions = Minecraft.Versions;
        }

        public List<Version> Versions
        {
            get { return (List<Version>)GetValue(VersionsProperty); }
            set { SetValue(VersionsProperty, value); }
        }
        public static readonly DependencyProperty VersionsProperty =
            DependencyProperty.Register("Versions", typeof(List<Version>), typeof(MainWindow), new PropertyMetadata(new List<Version>()));

        private Version _selected;
        public Version Selected
        {
            get { return _selected; }
            set
            {
                Set(ref _selected, value);
                Debug.WriteLine(Selected.ToString());
            }
        }

        #region INotifyPropertyChanged
        public event PropertyChangedEventHandler PropertyChanged;
        private void Set<T>(ref T storage, T value, [CallerMemberName] string propertyName = null)
        {
            if (Equals(storage, value))
            {
                return;
            }
            storage = value;
            OnPropertyChanged(propertyName);
        }
        protected virtual void OnPropertyChanged(string propertyName) => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        #endregion

        private void DecompileButton_Click(object sender, RoutedEventArgs e)
        {
            ProcessDialog.Visibility = Visibility.Visible;
            if (Selected is object)
                DecompileService.ExcuteDecompileAysnc(Selected, Side.Client);
        }

        private void ClearButton_Click(object sender, RoutedEventArgs e)
        {
            Directory.Exists("");
        }
    }
}
