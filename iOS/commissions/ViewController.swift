//
//  ViewController.swift
//  commissions
//
//  Created by Rob Miller on 2/16/20.
//  Copyright © 2020 Rob Miller. All rights reserved.
//

import UIKit
import AWSMobileClient

class ViewController: UIViewController, UITextFieldDelegate {
    
    let baseUrl = URL(string: "http://ec2-3-82-196-11.compute-1.amazonaws.com:5000")
    
    var accessoryRevPayout : Float = 0.35
    var tmpPayout : Float = 60.00
    var tmpMdPayout : Float = 180.00
    var vzpPayout : Float = 80.00
    var vzpMdPayout : Float = 210.00
    var tabletPayout : Float = 200.00
    var jetpackPayout : Float = 200.00
    var watchPayout : Float = 50.00
    var humPayout : Float = 50.00
    var humXPayout : Float = 200.00
    var nativeDialerPayout : Float = 200.00
    
    var accessoryRevBucketValue : Float = 0.0
    var tmpBucketValue : Float = 0
    var tmpMdBucketValue : Float = 0
    var vzpBucketValue : Float = 0
    var vzpMdBucketValue : Float = 0
    var tabletBucketValue : Float = 0
    var jetpackBucketValue : Float = 0
    var watchBucketValue : Float = 0
    var humBucketValue : Float = 0
    var humXBucketValue : Float = 0
    var nativeDialerBucketValue : Float = 0
    
    @IBOutlet weak var totalBucketLabel: UILabel!
    
    // accessory rev
    @IBOutlet weak var accessoryRevBucketLabel: UILabel!
    @IBOutlet weak var accessoryRevTextField: UITextField!
    @IBAction func accessoryRevChange(_ sender: Any) {
        let accessoryTotal = Float(accessoryRevTextField.text!) ?? 00.00
        accessoryRevBucketValue = accessoryTotal * accessoryRevPayout
        accessoryRevBucketLabel.text = "$\(accessoryRevBucketValue)"
        updateTotalBucket()
    }
    
    // tmp
    @IBOutlet weak var tmpBucketLabel: UILabel!
    @IBOutlet weak var tmpCountLabel: UILabel!
    @IBOutlet weak var tmpStepperValue: UIStepper!
    @IBAction func tmpStepper(_ sender: UIStepper) {
        tmpBucketValue = Float(tmpStepperValue.value) * tmpPayout
        tmpCountLabel.text = "\(tmpStepperValue.value)"
        tmpBucketLabel.text = "$\(tmpBucketValue)"
        updateTotalBucket()
    }
    
    // tmp md
    
    @IBOutlet weak var tmpMdBucketLabel: UILabel!
    @IBOutlet weak var tmpMdCountLabel: UILabel!
    @IBOutlet weak var tmpMdStepperValue: UIStepper!
    @IBAction func tmpMdStepper(_ sender: Any) {
        tmpMdBucketValue = Float(tmpMdStepperValue.value) * tmpMdPayout
        tmpMdCountLabel.text = "\(tmpMdStepperValue.value)"
        tmpMdBucketLabel.text = "$\(tmpMdBucketValue)"
        updateTotalBucket()
    }
    
    // vzp

    @IBOutlet weak var vzpBucketLabel: UILabel!
    @IBOutlet weak var vzpCountLabel: UILabel!
    @IBOutlet weak var vzpStepperValue: UIStepper!
    @IBAction func vzpStepper(_ sender: Any) {
        vzpBucketValue = Float(vzpStepperValue.value) * vzpPayout
        vzpCountLabel.text = "\(vzpStepperValue.value)"
        vzpBucketLabel.text = "$\(vzpBucketValue)"
        updateTotalBucket()
    }
    
    // vzp md
    @IBOutlet weak var vzpMdBucketLabel: UILabel!
    @IBOutlet weak var vzpMdCountLabel: UILabel!
    @IBOutlet weak var vzpMdStepperValue: UIStepper!
    @IBAction func vzpMdStepper(_ sender: Any) {
        vzpMdBucketValue = Float(vzpMdStepperValue.value) * vzpMdPayout
        vzpMdCountLabel.text = "\(vzpMdStepperValue.value)"
        vzpMdBucketLabel.text = "$\(vzpMdBucketValue)"
        updateTotalBucket()
    }
    
    // tablets
    @IBOutlet weak var tabletBucketLabel: UILabel!
    @IBOutlet weak var tabletCountLabel: UILabel!
    @IBOutlet weak var tabletStepperValue: UIStepper!
    @IBAction func tabletStepper(_ sender: Any) {
        tabletBucketValue = Float(tabletStepperValue.value) * tabletPayout
        tabletCountLabel.text = "\(tabletStepperValue.value)"
        tabletBucketLabel.text = "$\(tabletBucketValue)"
        updateTotalBucket()
    }
    
    // jetpacks
    @IBOutlet weak var jetpackBucketLabel: UILabel!
    @IBOutlet weak var jetpackCountLabel: UILabel!
    @IBOutlet weak var jetpackStepperValue: UIStepper!
    @IBAction func jetpackStepper(_ sender: Any) {
        jetpackBucketValue = Float(jetpackStepperValue.value) * jetpackPayout
        jetpackCountLabel.text = "\(jetpackStepperValue.value)"
        jetpackBucketLabel.text = "$\(jetpackBucketValue)"
        updateTotalBucket()
    }
    
    // watches
    @IBOutlet weak var watchBucketLabel: UILabel!
    @IBOutlet weak var watchCountLabel: UILabel!
    @IBOutlet weak var watchStepperValue: UIStepper!
    @IBAction func watchStepper(_ sender: Any) {
        watchBucketValue = Float(watchStepperValue.value) * watchPayout
        watchCountLabel.text = "\(watchStepperValue.value)"
        watchBucketLabel.text = "$\(watchBucketValue)"
        updateTotalBucket()
    }
    
    @IBOutlet weak var humBucketLabel: UILabel!
    @IBOutlet weak var humCountLabel: UILabel!
    @IBOutlet weak var humStepperValue: UIStepper!
    @IBAction func humStepper(_ sender: Any) {
        humBucketValue = Float(humStepperValue.value) * humPayout
        humCountLabel.text = "\(humStepperValue.value)"
        humBucketLabel.text = "$\(humBucketValue)"
        updateTotalBucket()
    }
    
    
    @IBOutlet weak var humXBucketLabel: UILabel!
    @IBOutlet weak var humXCountLabel: UILabel!
    @IBOutlet weak var humXStepperValue: UIStepper!
    @IBAction func humXStepper(_ sender: Any) {
        humXBucketValue = Float(humXStepperValue.value) * humXPayout
        humXCountLabel.text = "\(humXStepperValue.value)"
        humXBucketLabel.text = "$\(humXBucketValue)"
        updateTotalBucket()
    }
    
    @IBOutlet weak var nativeDialerBucketLabel: UILabel!
    @IBOutlet weak var nativeDialerCountLabel: UILabel!
    @IBOutlet weak var nativeDialerStepperValue: UIStepper!
    @IBAction func nativeDialerStepper(_ sender: Any) {
        nativeDialerBucketValue = Float(nativeDialerStepperValue.value) * nativeDialerPayout
        nativeDialerCountLabel.text = "\(nativeDialerStepperValue.value)"
        nativeDialerBucketLabel.text = "$\(nativeDialerBucketValue)"
        updateTotalBucket()
    }
    
    
    
    let datePicker = UIDatePicker()
    
    var dateFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateStyle = .long
        return formatter
    }
    
    @IBOutlet weak var selectedDate: UIButton!


    @IBAction func signOutButton(_ sender: Any) {
        print("Sign Out")
        AWSMobileClient.sharedInstance().signOut()
        showSignIn()
    }
    
    
    @IBAction func changeDateButton(_ sender: Any) {
        print("Change Date")
        showDatePicker()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        overrideUserInterfaceStyle = .light
        self.accessoryRevTextField.delegate = self
        
        initializeAWSMobileClient()
        
        // set date to current date
        let formatter = DateFormatter()
        //2016-12-08 03:37:22 +0000
        formatter.dateFormat = "MM/dd/yy"
        let now = Date()
        let dateString = formatter.string(from:now)
        NSLog("%@", dateString)
        
        selectedDate.titleLabel?.text = dateString
    }

    func initializeAWSMobileClient() {
        AWSMobileClient.sharedInstance().initialize { (userState, error) in
            if let userState = userState {
                switch(userState) {
                    case .signedIn:
                        print("Logged In")
                        print("Cognito Identity Id (authenticated): \(AWSMobileClient.sharedInstance().identityId))")
                                                
                    case .signedOut:
                        print("Logged out")
                        DispatchQueue.main.async {
                            self.showSignIn()
                        }
                    case .signedOutUserPoolsTokenInvalid: // User Pools refresh token INVALID
                        print("User Pools refresh token is invalid or expired.")
                        DispatchQueue.main.async {
                            self.showSignIn()
                        }
                    default:
                        AWSMobileClient.sharedInstance().signOut()
                    }
            } else if let error = error {
                print(error.localizedDescription)
            }
        }
    }
    
    func loadDataFromFlask() {
        // baseUrl + /load
    }
    
    func postDataToFlask() {
        print("postDataToFlask")
        //let postUrl = "/write"
        let totalUrl = baseUrl + postUrl
        guard let requestUrl = baseUrl else { fatalError() }
        
        var request = URLRequest(url: requestUrl)
        request.httpMethod = "POST"
        
        let postString = "userId=milro5c&date=02/28/20";
        
        // Set HTTP Request Body
        request.httpBody = postString.data(using: String.Encoding.utf8);
        
        // Perform HTTP Request
        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
            
            if let error = error {
                print("Error took place \(error)")
                return
            }
            
            if let data = data, let dataString = String(data: data, encoding: .utf8) {
                print("Response data string:\n \(dataString)")
            }
        }
        
        task.resume()
    }

    func showSignIn() {
        AWSMobileClient.sharedInstance().showSignIn(navigationController: self.navigationController!, {
            (userState, error) in
            if(error == nil) {
                DispatchQueue.main.async {
                    print("User successfully logged in")
                }
            }
        })
    }
    
     func showDatePicker(){
        print("show date picker")
            // Create a DatePicker
        let datePicker: UIDatePicker = UIDatePicker()
         
        datePicker.datePickerMode = UIDatePicker.Mode.date
        let pickerSize : CGSize = datePicker.sizeThatFits(CGSize.zero)
        
        // Posiiton date picket within a view
        datePicker.frame = CGRect(x: 10, y: 50, width: pickerSize.width, height: 200)
         
        // Set some of UIDatePicker properties
        datePicker.timeZone = NSTimeZone.local
        datePicker.backgroundColor = UIColor.white
         
        // Add an event to call onDidChangeDate function when value is changed.
        datePicker.addTarget(self, action:#selector(ViewController.datePickerValueChanged(_:)), for: .valueChanged)
    
        // Add DataPicker to the view

    }
    
    @objc func datePickerValueChanged(_ sender: UIDatePicker){
        
        // Create date formatter
        let dateFormatter: DateFormatter = DateFormatter()
        
        // Set date format
        dateFormatter.dateFormat = "MM/dd/yyyy hh:mm a"
        
        // Apply date format
        let selectedDate: String = dateFormatter.string(from: sender.date)
        
        print("Selected value \(selectedDate)")
    }

    func updateTotalBucket() {
        postDataToFlask()
        totalBucketLabel.text = "$\(accessoryRevBucketValue + tmpBucketValue + tmpMdBucketValue + vzpBucketValue + vzpMdBucketValue + tabletBucketValue + jetpackBucketValue + humBucketValue + humXBucketValue + nativeDialerBucketValue + watchBucketValue)"
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
}
